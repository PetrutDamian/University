package unique.damian.androidapp.core.data.offline

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import unique.damian.androidapp.core.data.Event
import unique.damian.androidapp.core.data.EventsRepository
import java.util.concurrent.TimeUnit.SECONDS

class OfflineWorker(
        context: Context,
        workerParams: WorkerParameters
) : Worker(context, workerParams) {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun doWork(): Result {

        // perform long running operation
        var offlineActions:HashMap<String,OfflineAction>  = OfflineHistory.getOfflineActions()
        if(offlineActions.entries.size>0) {
            offlineActions.entries.removeIf {
                when (it.value.type) {
                    "DELETED" -> {
                        CoroutineScope(Dispatchers.Main).launch { EventsRepository.delete(it.key) }
                        return@removeIf true
                    }
                    "UPDATED" -> {
                        CoroutineScope(Dispatchers.Main).launch { EventsRepository.update(it.value.event) }
                        return@removeIf true
                    }
                    "CREATED" -> {
                        val event = Event("", it.value.event.title,
                                it.value.event.description, it.value.event.location, it.value.event.reservationRequired, it.value.event.price, null)
                        CoroutineScope(Dispatchers.Main).launch { EventsRepository.save(event) }
                        EventsRepository.deleteOffline(it.key)
                        return@removeIf false
                    }
                }
                return@removeIf true
            }
        }
        return Result.success()
    }
}