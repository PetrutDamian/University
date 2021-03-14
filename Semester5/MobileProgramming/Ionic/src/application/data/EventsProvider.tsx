import React, { useCallback, useContext, useEffect, useReducer } from "react";
import PropTypes from "prop-types";
import { EventProps } from "./EventProps";
import {
  createItem,
  getEventsPage,
  newWebSocket,
  removeItem,
  updateItem,
} from "./remote/EventsApi";
import { AuthContext } from "../../auth/data/AuthProvider";
import { useNetworkStatus } from "../CustomHooks/UseNetworkStatus";
import { offlineHistory } from "./offline/OfflineEvents";
import {
  CONFLICT,
  DeleteEventFn,
  DELETE_EVENT_FAILED,
  DELETE_EVENT_OFFLINE,
  DELETE_EVENT_STARTED,
  DELETE_EVENT_SUCCEDED,
  END_CONFLICT,
  FETCH_ITEMS_FAILED,
  FETCH_ITEMS_STARTED,
  FETCH_ITEMS_SUCCEEDED,
  initialState,
  INITIAL_FETCH,
  NextPageFn,
  reducer,
  SaveItemFn,
  SAVE_EVENT_OFFLINE,
  SAVE_ITEM_FAILED,
  SAVE_ITEM_STARTED,
  SAVE_ITEM_SUCCEEDED,
  WRITE_PHOTO,
} from "./EventsReducer";
import { Plugins } from "@capacitor/core";
import { Photo, usePhotoGallery } from "../CustomHooks/UsePhotoGallery";

export const EventsContext = React.createContext(initialState);

interface EventsProviderProps {
  children: PropTypes.ReactNodeLike;
}

export const EventsProvider: React.FC<EventsProviderProps> = ({ children }) => {
  const { readPhoto } = usePhotoGallery();
  const network: boolean = useNetworkStatus();
  const { token, localData } = useContext(AuthContext);
  const [state, dispatch] = useReducer(reducer, initialState);
  const {
    page,
    events,
    fetching,
    fetchingError,
    saving,
    savingError,
    deleting,
    conflict,
    conflictMsg,
  } = state;
  useEffect(getItemsEffect, [token]);
  useEffect(webSocketEffect, [token]);
  useEffect(backOnlineEffect, [network, events]);

  const getNextPage = useCallback<NextPageFn>(nextPageCallback, [page, token]);
  const saveItem = useCallback<SaveItemFn>(saveItemCallback, [token, network]);
  const deleteEvent = useCallback<DeleteEventFn>(deleteEventCallback, [
    token,
    network,
  ]);
  const value = {
    page,
    events,
    fetching,
    fetchingError,
    saving,
    savingError,
    saveItem,
    deleteEvent,
    getNextPage,
    deleting,
    conflict,
    conflictMsg,
    conflictCallback: () => dispatch({ type: END_CONFLICT }),
  };
  return (
    <EventsContext.Provider value={value}>{children}</EventsContext.Provider>
  );
  function backOnlineEffect() {
    if (network === true) {
      for (const [key, value] of Object.entries(
        offlineHistory.getOfflineActions()
      )) {
        console.log(
          "entered with key:" +
            JSON.stringify(key) +
            "  value: " +
            JSON.stringify(value)
        );
        const actionType = value.type;
        switch (actionType) {
          case "delete":
            deleteEvent(key);
            break;
          case "create":
            dispatch({
              type: DELETE_EVENT_SUCCEDED,
              payload: { _id: key },
            });
            saveItem({ ...value.value, _id: undefined, offline: undefined });
            break;
          case "update":
            saveItem({ ...value.value, offline: undefined });
            break;
        }
        offlineHistory.removeAction(key);
        break;
      }
    }
  }
  function getItemsEffect() {
    let cancelled = false;
    if (!localData) {
      fetchItems();
      return () => {
        cancelled = true;
      };
    } else {
      const { Storage } = Plugins;
      (async () => {
        let events: any = await Storage.get({ key: "events" });
        let page: any = await Storage.get({ key: "page" });
        events = JSON.parse(events.value) as EventProps[];
        page = JSON.parse(page.value);
        console.log("here");
        console.log(JSON.stringify(events));
        for (let i = 0; i < events.length; i++)
          if (events[i].photo !== undefined) {
            const img = events[i].photo;
            const data = await readPhoto(img.filepath);
            img.webviewPath = `data:image/jpeg;base64,${data}`;
          }
        console.log("here2");
        dispatch({
          type: INITIAL_FETCH,
          payload: { items: events, page: page },
        });
      })();
    }
    async function fetchItems() {
      if (!token?.trim()) {
        return;
      }
      try {
        dispatch({ type: FETCH_ITEMS_STARTED });
        let events = await getEventsPage(token, 1);
        console.log("before resolve :\n" + JSON.stringify(events));
        await resolveEvents(events);
        console.log("after resolve :\n" + JSON.stringify(events));
        if (!cancelled) {
          dispatch({
            type: INITIAL_FETCH,
            payload: { items: events },
          });
        }
      } catch (error) {
        dispatch({ type: FETCH_ITEMS_FAILED, payload: { error } });
      }
    }
  }
  async function resolveEvents(events: EventProps[]) {
    for (let i = 0; i < events.length; i++) await resolveEvent(events[i]);
  }
  async function resolveEvent(event: EventProps) {
    if (event.photo !== undefined) {
      console.log("In resolve: event = " + JSON.stringify(event));
      const data = await readPhoto(event.photo.filepath);
      console.log("Data = " + data);
      event.photo.webviewPath = `data:image/jpeg;base64,${data}`;
      console.log(
        "In resolve: event = " + JSON.stringify(event) + " after resolve"
      );
    }
  }
  function webSocketEffect() {
    let canceled = false;
    let closeWebSocket: () => void;
    if (token?.trim()) {
      closeWebSocket = newWebSocket(token, (message) => {
        if (canceled) {
          return;
        }
        const { type, payload } = message;
        if (type === "conflict") {
          (async () => {
            await resolveEvent(payload);
            dispatch({ type: CONFLICT, payload: payload });
          })();
        }
        if (type === "created" || type === "updated" || type === "conflict") {
          (async () => {
            await resolveEvent(payload);
            dispatch({ type: SAVE_ITEM_SUCCEEDED, payload: { item: payload } });
          })();
        }
        if (type === "deleted")
          dispatch({ type: DELETE_EVENT_SUCCEDED, payload: payload });
      });
    }
    return () => {
      canceled = true;
      closeWebSocket?.();
    };
  }

  async function saveItemCallback(event: EventProps) {
    if (network === false) {
      dispatch({
        type: SAVE_EVENT_OFFLINE,
        payload: {
          event: event,
        },
      });
    } else {
      try {
        dispatch({ type: SAVE_ITEM_STARTED });
        const serverPhoto: (photo: Photo | undefined) => Photo | undefined = (
          photo: Photo | undefined
        ) => {
          if (photo === undefined) return undefined;
          return { filepath: photo.filepath, webviewPath: "" };
        };
        await (event._id
          ? updateItem(token, {
              ...event,
              photo: serverPhoto(event.photo),
            })
          : createItem(token, {
              ...event,
              photo: serverPhoto(event.photo),
            }));
      } catch (error) {
        dispatch({ type: SAVE_ITEM_FAILED, payload: { error } });
      }
    }
  }
  async function deleteEventCallback(id: string) {
    if (network === false)
      dispatch({ type: DELETE_EVENT_OFFLINE, payload: { _id: id } });
    else {
      try {
        dispatch({ type: DELETE_EVENT_STARTED });
        await removeItem(token, id);
      } catch (error) {
        dispatch({ type: DELETE_EVENT_FAILED, payload: { error } });
      }
    }
  }
  async function nextPageCallback() {
    try {
      dispatch({ type: FETCH_ITEMS_STARTED });
      const events = await getEventsPage(token, page);
      await resolveEvents(events);
      dispatch({ type: FETCH_ITEMS_SUCCEEDED, payload: { items: events } });
    } catch (error) {
      dispatch({ type: FETCH_ITEMS_FAILED, payload: { error } });
    }
  }
};

export default EventsProvider;
