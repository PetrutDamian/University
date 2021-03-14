package unique.damian.androidapp.core.data.offline

import unique.damian.androidapp.core.data.Event
import unique.damian.androidapp.core.data.OfflineStatus

class OfflineAction(public val type: String,public val event:Event){}

object OfflineHistory {
    private var offlineActions:HashMap<String,OfflineAction> = HashMap()
    fun add(event:Event){
        offlineActions[event._id] = OfflineAction("CREATED",
                Event("",event.title,event.description,event.location,event.reservationRequired,event.price, null))
    }
    fun delete(id:String){
        offlineActions[id] = OfflineAction("DELETED",Event(id,"","","",false,0,null))
    }
    fun update(event:Event){
        var type:OfflineStatus = OfflineStatus.UPDATED
        if(event.offline == OfflineStatus.CREATED)
            type = OfflineStatus.CREATED
        offlineActions[event._id] = OfflineAction(type.toString(),
                Event(event._id,event.title,event.description,event.location,event.reservationRequired,event.price,null))
    }
    fun removeAction(id:String){
        offlineActions.remove(id);
    }
    fun getOfflineActions():HashMap<String,OfflineAction>{
        return offlineActions
    }
}