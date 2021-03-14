import { EventProps } from "../EventProps";
type eventType = "create" | "update" | "delete";
 interface offlineEvent{
    type: eventType;
    value: EventProps;
}

type offlineEventsHistoryProps = {
    [id:string]: offlineEvent;
};


let offlineEventsHistory:offlineEventsHistoryProps = {}

export const offlineHistory =  {
    add:(event:EventProps)=>{
        if(event._id !== undefined){
            offlineEventsHistory[event._id] = {type:"create",value:event}
        }
    },
    update:(event:EventProps)=>{
        console.log("update in history");
        if(event._id !== undefined){
            console.log("not undefined");
            let eventType: eventType = "update";
            if(offlineEventsHistory[event._id] !== undefined && offlineEventsHistory[event._id].type === "create")
                eventType = "create";
            console.log("type: "+eventType);
            offlineEventsHistory[event._id] = {type:eventType,value:event}
            console.log("finished");
        }
    },
    delete:(id:string)=>{
        offlineEventsHistory[id] = {type:"delete",value:{_id:id,description:"",location:{latitude:0,longitude:0},price:0,reservationRequired:false,title:""}}
    },
    removeAction:(id:string|undefined)=>{
        if(id!==undefined)
        delete offlineEventsHistory[id];
    },
    getOfflineActions:()=>{
        return offlineEventsHistory;
    }
}


