import {
  IonPage,
  IonHeader,
  IonToolbar,
  IonTitle,
  IonContent,
  IonInput,
  IonLoading,
  IonButton,
} from "@ionic/react";
import React, { useContext, useState } from "react";
import { Redirect, RouteComponentProps } from "react-router";
import { AuthContext } from "../data/AuthProvider";

interface LoginState {
  username?: string;
  password?: string;
}

const Login: React.FC<RouteComponentProps> = ({ history }) => {
  const {
    isAuthenticated,
    isAuthenticating,
    login,
    authenticationError,
  } = useContext(AuthContext);
  const [state, setState] = useState<LoginState>({});
  const { username, password } = state;
  const handleLogin = () => {
    login?.(username, password);
  };
  if (isAuthenticated) {
    return <Redirect to={{ pathname: "/" }} />;
  }
  return (
    <IonPage>
      <IonHeader>
        <IonToolbar>
          <IonTitle>Login</IonTitle>
        </IonToolbar>
      </IonHeader>
      <IonContent>
        <IonInput
          placeholder="Username"
          value={username}
          onIonChange={(e) =>
            setState({
              ...state,
              username: e.detail.value || "",
            })
          }
        />
        <IonInput
          type="password"
          placeholder="Password"
          value={password}
          onIonChange={(e) =>
            setState({
              ...state,
              password: e.detail.value || "",
            })
          }
        />
        <IonLoading isOpen={isAuthenticating} />
        {authenticationError && (
          <div>{authenticationError.message || "Failed to authenticate"}</div>
        )}
        <IonButton onClick={handleLogin}>Login</IonButton>
      </IonContent>
    </IonPage>
  );
};

export default Login;
