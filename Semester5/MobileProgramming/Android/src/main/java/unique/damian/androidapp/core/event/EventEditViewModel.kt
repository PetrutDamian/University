package unique.damian.androidapp.core.event

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import unique.damian.androidapp.Others.NetworkLiveData
import unique.damian.androidapp.core.data.Event
import unique.damian.androidapp.core.data.EventsRepository
import unique.damian.androidapp.Others.TAG
import unique.damian.androidapp.Others.getJson

class EventEditViewModel : ViewModel() {
    private val mutableEvent =
        MutableLiveData<Event>().apply { value =
            Event(
                "",
                "",
                "",
                "",
                false,
                10, null
            )
        }
    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val event: LiveData<Event> = mutableEvent
    val fetching: LiveData<Boolean> = mutableFetching
    val fetchingError: LiveData<Exception> = mutableException
    val completed: LiveData<Boolean> = mutableCompleted

    fun loadEvent(eventId: String) {
        viewModelScope.launch {
            Log.i(TAG, "loadEvent... $eventId")
            mutableFetching.value = true
            mutableException.value = null
            try {
                mutableEvent.value = EventsRepository.load(eventId)
                Log.i(TAG, "loadEvent succeeded")
                Log.i(TAG,"mutableEvent.value = "+ (getJson()
                    .toJson(mutableEvent.value) ?:"" ))
                mutableFetching.value = false
            } catch (e: Exception) {
                Log.w(TAG, "loadEvent failed", e)
                mutableException.value = e
                mutableFetching.value = false
            }
        }
    }

    fun saveEvent(editedEvent: Event) {
            viewModelScope.launch {
                Log.i(TAG, "saveOrUpdateItem...");
                val event = mutableEvent.value ?: return@launch
                mutableFetching.value = true
                mutableException.value = null
                try {
                    if (event._id.isNotEmpty()) {
                        // mutableEvent.value =
                        if(NetworkLiveData.networkInfo)
                            EventsRepository.update(editedEvent)
                        else
                            EventsRepository.updateOffline(editedEvent)
                    } else {
                        //mutableEvent.value =
                        if(NetworkLiveData.networkInfo)
                            EventsRepository.save(editedEvent)
                        else
                            EventsRepository.saveOffline(editedEvent)
                        Log.i(TAG, "value after save:" + (getJson()?.toJson(mutableEvent.value)
                                ?: ""))
                    }
                    Log.i(TAG, "save/update succeeded");
                    mutableCompleted.value = true
                    mutableFetching.value = false
                } catch (e: Exception) {
                    Log.w(TAG, "save/update failed", e);
                    mutableException.value = e
                    mutableFetching.value = false
                }
            }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun deleteEvent(id:String){
        viewModelScope.launch {
            if(NetworkLiveData.networkInfo)
                EventsRepository.delete(id)
            else
                EventsRepository.deleteOffline(id)
        mutableCompleted.value = true
        }
    }
}