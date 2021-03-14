import {
  IonCard,
  IonCardContent,
  IonCardHeader,
  IonCardSubtitle,
  IonCardTitle,
  IonImg,
  IonItem,
  IonLabel,
  IonNote,
} from "@ionic/react";
import React from "react";
import { EventProps } from "../data/EventProps";

interface EventPropsExt extends EventProps {
  onEdit: (id?: string) => void;
}

export const Event: React.FC<EventPropsExt> = (props: EventPropsExt) => {
  if (props.offline !== undefined) {
    console.log("my props:" + props.offline);
  }
  return (
    <IonItem
      onClick={
        props.offline !== "deleted" ? () => props.onEdit(props._id) : () => {}
      }
    >
      <IonCard>
        <IonCardHeader>
          <IonCardTitle>{props.title}</IonCardTitle>
        </IonCardHeader>
        <IonCardContent>
          {props.offline !== undefined && (
            <IonNote color="danger" slot="end">
              Offline mode!({props.offline})
            </IonNote>
          )}
          {props.photo && (
            <IonImg
              style={{ width: "200px", height: "200px" }}
              src={props.photo.webviewPath}
            />
          )}
        </IonCardContent>
      </IonCard>
    </IonItem>
  );
};
