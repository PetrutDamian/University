import { Plugins } from "@capacitor/core";
import { useFilesystem } from "@ionic/react-hooks/filesystem";
import nextId from "react-id-generator";
import { Photo } from "../CustomHooks/UsePhotoGallery";
import { EventProps } from "./EventProps";
import { offlineHistory } from "./offline/OfflineEvents";

export const FETCH_ITEMS_STARTED = "FETCH_ITEMS_STARTED";
export const FETCH_ITEMS_SUCCEEDED = "FETCH_ITEMS_SUCCEEDED";
export const FETCH_ITEMS_FAILED = "FETCH_ITEMS_FAILED";
export const SAVE_ITEM_STARTED = "SAVE_ITEM_STARTED";
export const SAVE_ITEM_SUCCEEDED = "SAVE_ITEM_SUCCEEDED";
export const SAVE_ITEM_FAILED = "SAVE_ITEM_FAILED";
export const DELETE_EVENT_STARTED = "DELETE_EVENT_STARTED";
export const DELETE_EVENT_SUCCEDED = "DELETE_EVENT_SUCCEEDED";
export const DELETE_EVENT_FAILED = "DELETE_EVENT_FAILED";
export const SEND_OFFLINE_EVENTS = "SEND_OFFLINE_EVENTS";
export const INITIAL_FETCH = "INITIAL_FETCH";
export const DELETE_EVENT_OFFLINE = "DELETE_EVENT_OFFLINE";
export const SAVE_EVENT_OFFLINE = "SAVE_EVENT_OFFLINE";
export const CONFLICT = "CONFLICT";
export const END_CONFLICT = "END_CONFLICT"
export const WRITE_PHOTO = "WRITE_PHOTO";

export type SaveItemFn = (item: EventProps) => Promise<any>;
export type NextPageFn = () => Promise<any>;
export type DeleteEventFn = (id: string) => Promise<any>;
export interface EventsState {
  page: number;
  events: EventProps[];
  fetching: boolean;
  fetchingError?: Error | null;
  saving: boolean;
  savingError?: Error | null;
  saveItem?: SaveItemFn;
  getNextPage?: NextPageFn;
  deleteEvent?: DeleteEventFn;
  deleting: boolean;
  deletingError?: Error | null;
  conflictMsg:string;
  conflict:boolean;
  conflictCallback:()=>void;
  writePhotoFn?:(path: string, data: string) => Promise<void>
}

export const initialState: EventsState = {
  events: [],
  page: 1,
  fetching: false,
  saving: false,
  deleting: false,
  conflictMsg:"",
  conflict:false,
  conflictCallback:()=>{},
};

interface ActionProps {
  type: string;
  payload?: any;
}

export  const reducer: (state: EventsState, action: ActionProps) => EventsState = (
  state,
  { type, payload }
) => {
  function saveToStorage(stateEvents:EventProps[],page:number){
    const { Storage } = Plugins;
    (async () => {
      await Storage.set({
        key: "events",
        value: JSON.stringify(stateEvents.map(ev=>{
          if(ev.photo===undefined)
          return ev;
          return {...ev,photo:{...ev.photo,webviewPath:""}}
        })),
      });
      await Storage.set({
        key:"page",
        value:page.toString(),
      });
      })();
  }
  switch (type) {
    case WRITE_PHOTO:
      return {...state,writePhotoFn:payload.writePhoto};
    case CONFLICT:
      const event2 = state.events.find(ev=>ev._id===payload._id);
      let conf = false;
      if(event2?.version!==payload.version)
        conf = true;
      return {...state,conflict:conf,conflictMsg:"Update conflict!\n Found a newer version on the server.\n" +
      "Local version has been updated.\n Event title :  "+payload.title}
      case END_CONFLICT:
        return {...state,conflict:false};
    case FETCH_ITEMS_STARTED:
      return { ...state, fetching: true, fetchingError: null };
    case FETCH_ITEMS_SUCCEEDED:
      const oldEvents = [...(state.events || [])];
      const filteredEvents = payload.items.filter((ev: EventProps) => {
        const index = oldEvents.findIndex((evt) => evt._id === ev._id);
        if (index === -1) return true;
        else return false;
      });
      const eventsToSave = oldEvents.concat(filteredEvents);
      saveToStorage(eventsToSave,state.page+1);
      return {
        ...state,
        page: state.page + 1,
        events: eventsToSave,
        fetching: false,
      };
    case INITIAL_FETCH:
      saveToStorage(payload.items,payload.page || 2);
      return {
        ...state,
        page: payload.page===undefined?2:payload.page,
        events: payload.items,
        fetching: false,
      };
    case FETCH_ITEMS_FAILED:
      return { ...state, fetchingError: payload.error, fetching: false };
    case SAVE_ITEM_STARTED:
      return { ...state, savingError: null, saving: true };
    case SAVE_ITEM_SUCCEEDED:
      const events = [...(state.events || [])];
      const event = payload.item;
      const index = events.findIndex((ev) => ev._id === event._id);
      if (index === -1) events.splice(0, 0, event);
      else events[index] = event;
      saveToStorage(events,state.page);
      return { ...state, events, saving: false };
    case SAVE_ITEM_FAILED:
      return { ...state, savingError: payload.error, saving: false };
    case DELETE_EVENT_STARTED:
      return { ...state, deleting: true, deletingError: null };
    case DELETE_EVENT_FAILED:
      return { ...state, deletingError: payload.error, deleting: false };
    case DELETE_EVENT_SUCCEDED:
      const indexDel = state.events?.findIndex((ev) => ev._id === payload._id);
      if (indexDel !== undefined) state.events?.splice(indexDel, 1);
      saveToStorage(state.events,state.page);
      return { ...state, deleting: false };
    case DELETE_EVENT_OFFLINE:
      const eventIndex = state.events.findIndex((ev) => ev._id === payload._id);
      if (eventIndex !== undefined && state.events) {
        if (state.events[eventIndex].offline === "created") {
          const eventId = state.events[eventIndex]._id;
          if (eventId) {
            offlineHistory.removeAction(eventId);
            state.events.splice(eventIndex, 1);
          }
        } else {
          state.events[eventIndex].offline = "deleted";
          offlineHistory.delete(payload._id);
        }
      }
      saveToStorage(state.events,state.page);
      return state;
    case SAVE_EVENT_OFFLINE:
      const eventToSave: EventProps = payload.event;
      if (eventToSave._id === undefined) {
        const preparedEvent: EventProps = {
          ...eventToSave,
          offline: "created",
          _id: nextId(),
        };
        offlineHistory.add(preparedEvent);
        state.events.splice(0, 0, preparedEvent);
      } else {
        if (eventToSave.offline === undefined) {
          offlineHistory.update(eventToSave);
          const index = state.events?.findIndex(
            (ev) => ev._id === eventToSave._id
          );
          if (index !== undefined && state.events !== undefined)
            state.events[index] = { ...eventToSave, offline: "updated" };
        } else {
          if (eventToSave.offline === "created") {
            offlineHistory.add(eventToSave);
            const index = state.events?.findIndex(
              (ev) => ev._id === eventToSave._id
            );
            if (index !== undefined && state.events !== undefined)
              state.events[index] = eventToSave;
          } else if (eventToSave.offline === "updated") {
            offlineHistory.update(eventToSave);
            const index = state.events?.findIndex(
              (ev) => ev._id === eventToSave._id
            );
            if (index !== undefined && state.events !== undefined)
              state.events[index] = eventToSave;
          }
        }
      }
      console.log("exiting with state events :  "+JSON.stringify(state.events));
      saveToStorage(state.events,state.page);
      return state;
    default:
      return state;
  }
};
