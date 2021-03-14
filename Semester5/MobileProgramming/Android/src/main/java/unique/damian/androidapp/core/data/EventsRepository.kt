package unique.damian.androidapp.core.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import unique.damian.androidapp.Others.*
import unique.damian.androidapp.core.data.offline.OfflineAction
import unique.damian.androidapp.core.data.remote.EventApi
import unique.damian.androidapp.core.data.offline.OfflineHistory
import unique.damian.androidapp.core.data.offline.OfflineWorker
import unique.damian.androidapp.core.eventList.EventsListFragment

object EventsRepository {
    private var cachedEvents: MutableList<Event>? = null;
    suspend fun loadAll(): List<Event> {
        Log.i(TAG, "loadAll")
        if (cachedEvents != null) {
            return cachedEvents as List<Event>;
        }
        cachedEvents = mutableListOf()
        val events = EventApi.service.getAll()

        cachedEvents?.addAll(events)
        Log.i(TAG,"loadAll done, list is:"+ (getJson()
            ?.toJson(cachedEvents) ?: ""))
        return cachedEvents as List<Event>
    }
    fun clearEvents(){
        cachedEvents = null;
    }

    suspend fun load(eventId: String): Event? {
        Log.i(TAG, "load")
        Log.i(TAG,"cachhedEvents?"+(getJson()?.toJson(cachedEvents) ?: ""))
        Log.i(TAG,"eventId to find:"+eventId)
            for (event: Event in cachedEvents!!){
                Log.i(TAG,"current event.Id "+ event._id +"and idEvent: "+eventId)
                if(event._id==eventId) {

                    Log.i(TAG,"found event"+ getJson()
                        ?.toJson(event)?:"")
                    return event;
                }
            }
        return null;
    }
    suspend fun save(event: Event): Event {
        Log.i(TAG, "save")
        val createdEvent = EventApi.service.create(event)
        Log.i(TAG,"response after api call:"+ (getJson()?.toJson(createdEvent) ?: ""))
        return createdEvent
    }

    suspend fun update(event: Event): Event {
        Log.i(TAG, "update")
        return EventApi.service.update(event._id, event)
    }
    suspend fun delete(id:String){
        EventApi.service.delete(id)
    }
    fun saveOffline(event:Event){
        val preparedEvent:Event =
                Event(uniqueId(),event.title,event.description,event.location,event.reservationRequired,event.price,OfflineStatus.CREATED)
        OfflineHistory.add(preparedEvent)
        cachedEvents?.add((preparedEvent))
    }

    fun updateOffline(event:Event){
        val index = cachedEvents?.indexOfFirst { it._id==event._id }
        if(index!=null){
            if(event.offline == null){
                OfflineHistory.update(event)
                event.offline = OfflineStatus.UPDATED
            }
            else if (event.offline == OfflineStatus.CREATED)
                OfflineHistory.add(event)
            else if(event.offline == OfflineStatus.UPDATED)
                OfflineHistory.update(event)
            cachedEvents?.set(index,event)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun deleteOffline(id:String){
        val index = cachedEvents?.indexOfFirst { it._id == id }
        if(index!=null){
            if(cachedEvents?.get(index)?.offline == OfflineStatus.CREATED) {
                cachedEvents?.removeIf { it._id == id }
                OfflineHistory.removeAction(id)
            }
            else{
                OfflineHistory.delete(id)
                val event:Event = cachedEvents?.get(index) ?:Event("","","","",false,0)
                event.offline = OfflineStatus.DELETED
                cachedEvents?.set(index,event)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun backOnline(){


        var offlineActions:HashMap<String,OfflineAction>  = OfflineHistory.getOfflineActions()
        offlineActions.entries.removeIf{
            when (it.value.type){
                "DELETED"->{
                    CoroutineScope(Dispatchers.Main).launch { delete(it.key) }
                    return@removeIf true
                }
                "UPDATED"->{
                    CoroutineScope(Dispatchers.Main).launch { update(it.value.event) }
                    return@removeIf true
                }
                "CREATED"->{
                    val event = Event("",it.value.event.title,
                            it.value.event.description,it.value.event.location,it.value.event.reservationRequired,it.value.event.price,null)
                    CoroutineScope(Dispatchers.Main).launch { save(event) }
                    deleteOffline(it.key)
                    return@removeIf false
                }
            }
            return@removeIf true
        }
    }

    suspend fun receiveNotifications() {
        while (EventsListFragment.listenForNotifications) {
            val notification = WebSocketNotifications.channel.receive();
            Log.d("EventsListFragment", "Notification received in list view model")

            when (notification.type) {
                "deleted" -> {
                    Log.d("delete","delete");
                    val removeNotif = getJson().fromJson(
                        getJson().toJson(notification.payload),
                        RemoveNotification::class.java
                    )
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        cachedEvents?.removeIf{it._id==removeNotif._id}
                    }
                }
                "updated"-> {
                    Log.d("Update","Update")
                    val updatedEvent:Event = getJson().fromJson(getJson().toJson(notification.payload),Event::class.java)
                    val index = cachedEvents?.indexOfFirst { it._id==updatedEvent._id }

                    if (index != null)
                        cachedEvents?.set(index,updatedEvent)
                    Log.d("Update Index", "Index:$index")
                }
                "created"->{
                    val newEvent = getJson().fromJson(getJson().toJson(notification.payload),Event::class.java)
                    cachedEvents?.add(newEvent)
                }
            }
        }

    }
}