import { IonChip, IonLabel } from "@ionic/react";
import React from "react";
import { useNetworkStatus } from "../CustomHooks/UseNetworkStatus";

export const DisplayNetworkStatus: React.FC = () => {
  const state: boolean = useNetworkStatus();
  if (state)
    return (
      <IonChip>
        <IonLabel color="success">Online</IonLabel>
      </IonChip>
    );
  else
    return (
      <IonChip>
        <IonLabel color="danger">Offline</IonLabel>
      </IonChip>
    );
};
