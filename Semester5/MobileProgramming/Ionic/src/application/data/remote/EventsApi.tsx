import axios from "axios";
import { EventProps } from "../EventProps";

interface ResponseProps<T> {
  data: T;
}

function withLogs<T>(
  promise: Promise<ResponseProps<T>>,
  fnName: string
): Promise<T> {
  return promise
    .then((res) => {
      return Promise.resolve(res.data);
    })
    .catch((err) => {
      return Promise.reject(err);
    });
}

const config = {
  headers: {
    "Content-Type": "application/json",
  },
};
const authConfig = (token?: string) => ({
  headers: {
    "Content-Type": "application/json",
    Authorization: `Bearer ${token}`,
  },
});
const authConfigVersion = (token: string, version?: number) => ({
  headers: {
    "Content-Type": "application/json",
    Authorization: `Bearer ${token}`,
    ETag: version,
  },
});

const baseUrl = "http://localhost:3000";
const itemUrl = `${baseUrl}/api/events`;

export const getItems: (token: string) => Promise<EventProps[]> = (token) => {
  return withLogs(axios.get(`${itemUrl}/`, authConfig(token)), "getItems");
};

export const getEventsPage: (
  token: string,
  page: number
) => Promise<EventProps[]> = (token, page) => {
  return withLogs(
    axios.get(`${itemUrl}/page/${page}`, authConfig(token)),
    `Get items on page: ${page}`
  );
};

export const createItem: (
  token: string,
  item: EventProps
) => Promise<EventProps[]> = (token, item) => {
  console.log("Create item: " + JSON.stringify(item));
  return withLogs(
    axios.post(`${itemUrl}/`, item, authConfig(token)),
    "createItem"
  );
};

export const updateItem: (
  token: string,
  item: EventProps
) => Promise<EventProps[]> = (token, item) => {
  return withLogs(
    axios.put(
      `${itemUrl}/${item._id}`,
      item,
      authConfigVersion(token, item.version)
    ),
    "updateItem"
  );
};
export const removeItem: (token: string, id: string) => Promise<any> = (
  token,
  id
) => {
  return withLogs(
    axios.delete(`${itemUrl}/${id}`, authConfig(token)),
    "deleteItem"
  );
};
interface MessageData {
  type: string;
  payload: EventProps;
}

export const newWebSocket = (
  token: string,
  onMessage: (data: MessageData) => void
) => {
  const ws = new WebSocket(`ws://localhost:3000`);
  ws.onopen = () => {
    ws.send(JSON.stringify({ type: "authorization", payload: { token } }));
  };
  ws.onclose = () => {};
  ws.onerror = (error) => {};
  ws.onmessage = (messageEvent) => {
    onMessage(JSON.parse(messageEvent.data));
  };
  return () => {
    ws.close();
  };
};
