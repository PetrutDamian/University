import { laptop } from "ionicons/icons";
import React from "react";
import {
  withScriptjs,
  withGoogleMap,
  GoogleMap,
  Marker,
} from "react-google-maps";
import { compose, withProps } from "recompose";
import { mapsApiKey } from "./mapsApiKey";

interface MyMapProps {
  lat?: number;
  lng?: number;
  positionChanged: (lat: number, long: number) => void;
}

export const MyMap = compose<MyMapProps, any>(
  withProps({
    googleMapURL: `https://maps.googleapis.com/maps/api/js?key=${mapsApiKey}&v=3.exp&libraries=geometry,drawing,places`,
    loadingElement: <div style={{ height: `100%` }} />,
    containerElement: <div style={{ height: `100%` }} />,
    mapElement: <div style={{ height: `100%` }} />,
  }),
  withScriptjs,
  withGoogleMap
)((props) => (
  <GoogleMap defaultZoom={8} defaultCenter={{ lat: props.lat, lng: props.lng }}>
    <Marker
      onDragEnd={(e) => {
        props.positionChanged(e.latLng.lat(), e.latLng.lng());
      }}
      draggable={true}
      position={{ lat: props.lat, lng: props.lng }}
    />
  </GoogleMap>
));
