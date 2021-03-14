import React from "react";
import { Redirect, Route } from "react-router-dom";
import { IonApp, IonRouterOutlet } from "@ionic/react";
import { IonReactRouter } from "@ionic/react-router";

/* Core CSS required for Ionic components to work properly */
import "@ionic/react/css/core.css";

/* Basic CSS for apps built with Ionic */
import "@ionic/react/css/normalize.css";
import "@ionic/react/css/structure.css";
import "@ionic/react/css/typography.css";

/* Optional CSS utils that can be commented out */
import "@ionic/react/css/padding.css";
import "@ionic/react/css/float-elements.css";
import "@ionic/react/css/text-alignment.css";
import "@ionic/react/css/text-transformation.css";
import "@ionic/react/css/flex-utils.css";
import "@ionic/react/css/display.css";

/* Theme variables */
import "./theme/variables.css";
import EventsProvider from "./application/data/EventsProvider";
import EventsList from "./application/event_list_components/EventsList";
import { AuthProvider } from "./auth/data/AuthProvider";
import { PrivateRoute } from "./auth/login_components/PrivateRoute";
import Login from "./auth/login_components/Login";
import EditEvent from "./application/event_components/EditEvent";

const App: React.FC = () => (
  <IonApp>
    <IonReactRouter>
      <IonRouterOutlet>
        <AuthProvider>
          <Route path="/login" exact={true} component={Login} />
          <EventsProvider>
            <PrivateRoute path="/events" component={EventsList} exact={true} />
            <PrivateRoute path="/event" component={EditEvent} exact={true} />
            <PrivateRoute
              path="/event/:id"
              component={EditEvent}
              exact={true}
            />
          </EventsProvider>
          <Route exact path="/" render={() => <Redirect to="/events" />} />
        </AuthProvider>
      </IonRouterOutlet>
    </IonReactRouter>
  </IonApp>
);

export default App;