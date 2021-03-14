import {
  createAnimation,
  IonActionSheet,
  IonAlert,
  IonButton,
  IonButtons,
  IonCheckbox,
  IonCol,
  IonContent,
  IonGrid,
  IonHeader,
  IonImg,
  IonInput,
  IonItem,
  IonLabel,
  IonPage,
  IonRow,
  IonTextarea,
  IonTitle,
  IonToolbar,
} from "@ionic/react";
import { trash } from "ionicons/icons";
import React, { useContext, useEffect, useState } from "react";
import { RouteComponentProps } from "react-router";
import { Photo, usePhotoGallery } from "../CustomHooks/UsePhotoGallery";
import { EventsContext } from "../data/EventsProvider";
import { getLocation, MyLocation } from "../data/remote/GetLocation";
import { MyMap } from "./MyMap";
import { MyModal } from "./MyModal";

interface EditEventProps
  extends RouteComponentProps<{
    id?: string;
  }> {}

const EditEvent: React.FC<EditEventProps> = ({ history, match }) => {
  const { events, saveItem, deleteEvent } = useContext(EventsContext);
  const [title, setTitle] = useState<string>("");
  const [description, setDescription] = useState<string>("");
  const [price, setPrice] = useState<number>(10);
  const [location, setLocation] = useState<MyLocation | undefined>(
    getLocation()
  );
  const [reservationRequired, setReservationRequired] = useState<boolean>(
    false
  );
  const [version, setVersion] = useState<number>();
  const [offline, setOffline] = useState<string>();
  const [showMaps, setShowMaps] = useState<boolean>(false);
  const [id, setId] = useState<string>();
  const [photo, setPhoto] = useState<Photo>();
  const [deletingPhoto, setDeletingPhoto] = useState<boolean>(false);
  const { takePhoto, deletePhoto } = usePhotoGallery();
  useEffect(() => {
    const id = match.params.id || "";
    const event = events.find((event) => event._id === id);
    if (event) {
      setTitle(event.title);
      setDescription(event.description);
      setLocation(event.location);
      setReservationRequired(event.reservationRequired);
      setPrice(event.price);
      setId(event._id);
      setOffline(event.offline);
      setVersion(event.version);
      setPhoto(event.photo);
    }
  }, [match.params.id, events]);

  const handleSave = () => {
    if (description !== "" && title !== "") {
      const edited = {
        _id: id,
        title,
        description,
        price,
        location: location || getLocation(),
        reservationRequired,
        offline,
        version,
        photo,
      };
      saveItem && saveItem(edited).then(() => history.goBack());
    } else {
      animateRedLabels();
    }
  };
  async function animateRedLabels() {
    const titleLabel = document.querySelector(".titleLabel");
    const descLabel = document.querySelector(".descriptionLabel");
    const btn = document.querySelector(".buttonAnimated");
    if (titleLabel && descLabel && btn) {
      console.log("da");
      const animation1 = createAnimation()
        .addElement(titleLabel)
        .beforeStyles({ color: "red" })
        .fromTo("transform", "scale(0.85)", "scale(1.25)")
        .afterStyles({ color: "black" });
      const animation2 = createAnimation()
        .addElement(descLabel)
        .beforeStyles({ color: "red" })
        .fromTo("transform", "scale(0.85)", "scale(1.25)")
        .afterStyles({ color: "black" });
      const groupAnimation = createAnimation()
        .duration(250)
        .addAnimation([animation1, animation2])
        .direction("alternate")
        .iterations(2);

      const animation3 = createAnimation()
        .addElement(btn)
        .duration(250)
        .to("color", "red")
        .fromTo("transform", "scale(1)", "scale(1.1)")
        .direction("alternate")
        .iterations(2);
      await groupAnimation.play();
      await animation3.play();
    }
  }
  const handleDelete = (id: string) => {
    deleteEvent && deleteEvent(id).then(() => history.goBack());
  };
  useEffect(simpleAnimationEffect, [photo]);
  function simpleAnimationEffect() {
    const img = document.querySelector(".animatedImg");
    if (img) {
      const animation = createAnimation()
        .addElement(img)
        .duration(1000)
        .keyframes([
          { offset: 0, transform: "scale(0)", opacity: "0" },
          { offset: 1, transform: "scale(1)", opacity: "1" },
        ]);
      animation.play();
    }
  }
  return (
    <IonPage>
      <IonHeader>
        <IonToolbar>
          <IonTitle>EditEvent</IonTitle>
          <IonButtons slot="end">
            {id && (
              <IonButton onClick={() => handleDelete(id)}>Delete</IonButton>
            )}
            <IonButton onClick={handleSave}>Save</IonButton>
          </IonButtons>
        </IonToolbar>
      </IonHeader>
      <IonContent>
        <IonGrid>
          <IonRow>
            <IonCol size="2">
              <IonItem>
                <IonLabel className="titleLabel" position="floating">
                  Title
                </IonLabel>
                <IonInput
                  value={title}
                  onIonChange={(e) => setTitle(e.detail.value || "")}
                />
              </IonItem>
            </IonCol>
          </IonRow>
          <IonRow>
            <IonCol size="8">
              <IonItem>
                <IonLabel className="descriptionLabel" position="floating">
                  Description
                </IonLabel>
                <IonTextarea
                  value={description}
                  onIonChange={(e) => setDescription(e.detail.value || "")}
                />
              </IonItem>
            </IonCol>
          </IonRow>
          <IonRow>
            <IonCol size="2">
              <IonItem>
                <IonLabel position="floating">Price</IonLabel>
                <IonInput
                  type="number"
                  value={price}
                  onIonChange={(e) => setPrice(Number(e.detail.value))}
                />
              </IonItem>
            </IonCol>
            <IonCol size="2">
              <IonItem>
                <IonLabel>ReservationRequired</IonLabel>
                <IonCheckbox
                  checked={reservationRequired}
                  onIonChange={(e) => setReservationRequired(e.detail.checked)}
                />
              </IonItem>
            </IonCol>
          </IonRow>
          <IonRow>
            <IonCol size="2">
              <IonButton
                className="buttonAnimated"
                onClick={() => setShowMaps(true)}
              >
                Location
              </IonButton>
              <MyModal isOpen={showMaps}>
                <IonButton onClick={() => setShowMaps(false)}>
                  Close Map
                </IonButton>
                <MyMap
                  positionChanged={(lat: number, long: number) => {
                    setLocation({ latitude: lat, longitude: long });
                  }}
                  lat={location?.latitude}
                  lng={location?.longitude}
                />
              </MyModal>
            </IonCol>
          </IonRow>
          <IonRow>
            <IonCol size="2">
              <IonLabel>Photo: </IonLabel>
              {(photo && (
                <IonImg
                  className="animatedImg"
                  onClick={() => setDeletingPhoto(true)}
                  src={photo.webviewPath}
                />
              )) || (
                <IonButton
                  style={{ verticalAlign: "middle" }}
                  onClick={() =>
                    (async () => {
                      const image = await takePhoto();
                      setPhoto(image);
                    })()
                  }
                >
                  Assign photo
                </IonButton>
              )}
            </IonCol>
          </IonRow>
        </IonGrid>
        <IonActionSheet
          isOpen={deletingPhoto}
          buttons={[
            {
              text: "Delete",
              role: "destructive",
              icon: trash,
              handler: () => {
                if (photo) deletePhoto(photo?.filepath);
                setDeletingPhoto(false);
                setPhoto(undefined);
              },
            },
            {
              text: "Cancel",
              role: "cancel",
              icon: "close",
            },
          ]}
          onDidDismiss={() => setDeletingPhoto(false)}
        />
      </IonContent>
    </IonPage>
  );
};

export default EditEvent;
