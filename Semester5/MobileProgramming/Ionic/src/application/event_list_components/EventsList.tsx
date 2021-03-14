import {
  IonAlert,
  IonButton,
  IonButtons,
  IonContent,
  IonFab,
  IonFabButton,
  IonHeader,
  IonIcon,
  IonInfiniteScroll,
  IonInfiniteScrollContent,
  IonList,
  IonLoading,
  IonPage,
  IonSearchbar,
  IonTitle,
  IonToolbar,
} from "@ionic/react";
import React, { useContext, useState } from "react";
import { add } from "ionicons/icons";
import { RouteComponentProps } from "react-router";
import { EventsContext } from "../data/EventsProvider";
import { AuthContext } from "../../auth/data/AuthProvider";
import { DisplayNetworkStatus } from "../other_components/DisplayNetworkStatus";
import { Event } from "../event_components/Event";

const EventsList: React.FC<RouteComponentProps> = ({ history }) => {
  const [disabled] = useState(false);
  const {
    events,
    fetching,
    fetchingError,
    getNextPage,
    conflictMsg,
    conflict,
    conflictCallback,
  } = useContext(EventsContext);
  const { logout } = useContext(AuthContext);
  const [filter, setFilter] = useState("");
  async function getTheNextPage(e: CustomEvent<void>) {
    if (getNextPage != null) {
      await getNextPage();
      (e.target as HTMLIonInfiniteScrollElement).complete();
    }
  }
  return (
    <IonPage>
      <IonHeader>
        <IonToolbar>
          <IonTitle>
            Ionic app <DisplayNetworkStatus />
          </IonTitle>
          <IonButtons slot="end">
            <IonButton onClick={logout}>Logout</IonButton>
          </IonButtons>
        </IonToolbar>
      </IonHeader>
      <IonContent fullscreen>
        <IonAlert
          isOpen={conflict}
          message={conflictMsg}
          buttons={["OK"]}
          header="Conflict!"
          onDidDismiss={conflictCallback}
        />
        <IonSearchbar
          showCancelButton="never"
          value={filter}
          debounce={300}
          onIonChange={(e) => setFilter(e.detail.value!)}
        ></IonSearchbar>
        <IonLoading isOpen={fetching} message="Fetching events" />
        <IonList>
          {events
            .filter((e) => e.title.includes(filter))
            .map((x) => (
              <Event
                key={x._id}
                _id={x._id}
                description={x.description}
                location={x.location}
                title={x.title}
                price={x.price}
                reservationRequired={x.reservationRequired}
                photo={x.photo}
                onEdit={(id) => {
                  history.push(`/event/${id}`);
                }}
                offline={x.offline}
              ></Event>
            ))}
        </IonList>

        {fetchingError && (
          <div>{fetchingError.message || "Failed to fetch events"}</div>
        )}
        <IonFab vertical="bottom" horizontal="end" slot="fixed">
          <IonFabButton onClick={() => history.push("/event")}>
            <IonIcon icon={add} />
          </IonFabButton>
        </IonFab>
        <IonInfiniteScroll
          position="bottom"
          threshold="150px"
          disabled={disabled}
          onIonInfinite={(e: CustomEvent<void>) => {
            getTheNextPage(e);
          }}
        >
          <IonInfiniteScrollContent loadingText="Loading events"></IonInfiniteScrollContent>
        </IonInfiniteScroll>
      </IonContent>
    </IonPage>
  );
};
export default EventsList;
