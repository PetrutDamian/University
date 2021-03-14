import React, { useCallback, useEffect, useState } from "react";
import PropTypes from "prop-types";
import { login as loginApi } from "./remote/authApi";
import { Plugins } from "@capacitor/core";

type LoginFn = (username?: string, password?: string) => void;
type LogoutFn = () => void;

export interface AuthState {
  authenticationError: Error | null;
  isAuthenticated: boolean;
  isAuthenticating: boolean;
  login?: LoginFn;
  pendingAuthentication?: boolean;
  username?: string;
  password?: string;
  token: string;
  logout?: () => void;
  localData: boolean;
}

const initialState: AuthState = {
  isAuthenticated: false,
  isAuthenticating: false,
  authenticationError: null,
  pendingAuthentication: false,
  token: "",
  localData: false,
};

export const AuthContext = React.createContext<AuthState>(initialState);

interface AuthProviderProps {
  children: PropTypes.ReactNodeLike;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [state, setState] = useState<AuthState>(initialState);
  const {
    isAuthenticated,
    isAuthenticating,
    authenticationError,
    pendingAuthentication,
    token,
    localData,
  } = state;
  const login = useCallback<LoginFn>(loginCallback, []);
  const logout = useCallback<LogoutFn>(logoutCallBack, []);
  useEffect(initialSetupEffect, []);
  useEffect(authenticationEffect, [pendingAuthentication]);
  const value = {
    isAuthenticated,
    login,
    isAuthenticating,
    authenticationError,
    token,
    logout,
    localData,
  };
  //log("render");
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;

  function loginCallback(username?: string, password?: string): void {
    console.log(
      "Entered Login function callback, will make pendingAuthentification:true and set username&password"
    );
    setState({
      ...state,
      pendingAuthentication: true,
      username,
      password,
    });
  }
  function logoutCallBack() {
    const { Storage } = Plugins;
    Storage.remove({ key: "token" });
    setState({
      ...state,
      isAuthenticated: false,
      token: "",
    });
  }
  function initialSetupEffect() {
    const { Storage } = Plugins;
    (async () => {
      let result: any = await Storage.get({ key: "token" });
      if (result.value !== null) {
        let resultObj = JSON.parse(result.value);
        console.log(
          "Inside AuthentificationEffect, pending is :" +
            pendingAuthentication +
            " result from Storage is not null, result.value = " +
            JSON.stringify(resultObj)
        );
        if (resultObj.time > Date.now())
          setState({
            ...state,
            isAuthenticated: true,
            token: resultObj.token,
            localData: true,
          });
      }
    })();
  }

  function authenticationEffect() {
    let canceled = false;
    authenticate();
    return () => {
      canceled = true;
    };
    async function authenticate() {
      if (!pendingAuthentication) {
        console.log(
          `Entered authentificate() , pendingAuthentification is false, returning`
        );
        return;
      }
      try {
        console.log("Inside authentificate() setting isAuthentifcating: True");
        setState({
          ...state,
          isAuthenticating: true,
        });
        const { username, password } = state;
        const response = await loginApi(username, password);
        console.log(
          "Response received from loginAPi: " + JSON.stringify(response)
        );
        if (canceled) {
          console.log("Cancelled, ignoring response");
          return;
        }
        const { Storage } = Plugins;
        let expiryDate = new Date();
        expiryDate.setMinutes(expiryDate.getMinutes() + 2);
        await Storage.set({
          key: "token",
          value: JSON.stringify({
            token: response.data.token,
            time: expiryDate.getTime(),
          }),
        });
        setState({
          ...state,
          token: response.data.token,
          pendingAuthentication: false,
          isAuthenticated: true,
          isAuthenticating: false,
        });
      } catch (error) {
        if (canceled) {
          return;
        }
        //log("authenticate failed");
        setState({
          ...state,
          authenticationError: error,
          pendingAuthentication: false,
          isAuthenticating: false,
        });
      }
    }
  }
};
