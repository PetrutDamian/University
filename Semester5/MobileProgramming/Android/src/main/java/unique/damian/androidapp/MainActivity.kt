package unique.damian.androidapp

import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.work.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import unique.damian.androidapp.Others.Api
import unique.damian.androidapp.Others.NetworkLiveData
import unique.damian.androidapp.Others.TAG
import unique.damian.androidapp.Others.WebSocketNotifications
import unique.damian.androidapp.auth.data.AuthRepository
import unique.damian.androidapp.core.data.EventsRepository
import unique.damian.androidapp.core.data.offline.OfflineWorker
import unique.damian.androidapp.core.eventList.EventsListViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkLiveData: NetworkLiveData

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        connectivityManager = getSystemService(android.net.ConnectivityManager::class.java)
        networkLiveData = NetworkLiveData(connectivityManager)
        networkLiveData.observe(this, Observer{
            Log.v(TAG, "recieved notification from live data : $it")
            NetworkLiveData.networkInfo = it
            onlineSwitch.isChecked = it;
            if(it==true)
                backOnline()
        })
    }

    fun backOnline(){
        Api.tokenInterceptor.token?.let { WebSocketNotifications.initializeWebSocket(it) }
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        val inputData = Data.Builder()
                .build()
        val myWork = OneTimeWorkRequest.Builder(OfflineWorker::class.java)
                .setConstraints(constraints)
                .setInputData(inputData)
                .build()
        val workId = myWork.id
        WorkManager.getInstance(this).apply {
            // enqueue Work
            enqueue(myWork)
            // observe work status
            getWorkInfoByIdLiveData(workId)
                    .observe(this@MainActivity) { status ->
                        val isFinished = status?.state?.isFinished
                        Log.d(TAG, "Job $workId; finished: $isFinished")
                    }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

}