package unique.damian.androidapp.core.eventList

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import unique.damian.androidapp.Others.*
import unique.damian.androidapp.core.data.Event
import unique.damian.androidapp.core.data.EventsRepository

class EventsListViewModel :ViewModel() {

    private val mutableEvents = MutableLiveData<List<Event>>().apply { value = emptyList() }
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val events: LiveData<List<Event>> = mutableEvents
    val loading: LiveData<Boolean> = mutableLoading
    val loadingError: LiveData<Exception> = mutableException


    fun loadEvents() {
        viewModelScope.launch {
            Log.v(TAG, "loadEvents...");
            mutableLoading.value = true
            mutableException.value = null
            try {
                mutableEvents.value = EventsRepository.loadAll()
                Log.d(TAG, "loadEvents succeeded");
                mutableLoading.value = false
            } catch (e: Exception) {
                Log.w(TAG, "loadEvents failed", e);
                mutableException.value = e
                mutableLoading.value = false
            }
        }
    }


}