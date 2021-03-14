import axios from "axios";

const baseUrl = "localhost:3000";
const config = {
  headers: {
    "Content-Type": "application/json",
  },
};
const authUrl = `http://${baseUrl}/api/auth/login`;

export interface AuthProps {
  data: { token: string };
}

export const login: (
  username?: string,
  password?: string
) => Promise<AuthProps> = (username, password) => {
  return axios.post(authUrl, { username, password }, config);
};
