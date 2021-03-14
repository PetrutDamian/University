import { GeolocationPosition, Plugins } from '@capacitor/core';
import { useFilesystem } from '@ionic/react-hooks/filesystem';
import { useEffect, useState } from 'react';

const { Geolocation } = Plugins;

export interface MyLocation{
    latitude:number,
    longitude:number
}
 let location:MyLocation;
(async ()=>{
    const pozition = await Geolocation.getCurrentPosition();
    location = {latitude:pozition.coords.latitude,longitude:pozition.coords.longitude};
})()

export const getLocation = ()=>{
    return location;
}