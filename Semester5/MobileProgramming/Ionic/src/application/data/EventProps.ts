import { Photo } from "../CustomHooks/UsePhotoGallery";
import { MyLocation } from "./remote/GetLocation";

export interface EventProps{
    _id?:string;
    title:string;
    description:string;
    location:MyLocation;
    price:number;
    reservationRequired:boolean;
    offline?:string;
    version?:number;
    photo?:Photo;
}