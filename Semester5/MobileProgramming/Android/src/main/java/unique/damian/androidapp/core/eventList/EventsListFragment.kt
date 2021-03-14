package unique.damian.androidapp.core.eventList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_events_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import unique.damian.androidapp.Others.Api
import unique.damian.androidapp.Others.Notification
import unique.damian.androidapp.auth.data.AuthRepository
import unique.damian.androidapp.Others.TAG
import unique.damian.androidapp.Others.WebSocketNotifications
import unique.damian.androidapp.R
import unique.damian.androidapp.core.data.EventsRepository

class EventsListFragment:Fragment(){
    private lateinit var eventsListAdapter: EventsListAdapter
    private lateinit var eventsModel: EventsListViewModel
    companion object{
        var listenForNotifications = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
    }

    override fun onStart() {
        super.onStart()
        listenForNotifications = true
        CoroutineScope(Dispatchers.Main).launch { EventsRepository.receiveNotifications() }
    }

    override fun onStop() {
        super.onStop()
        listenForNotifications = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_events_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(!AuthRepository.isLoggedIn){
            findNavController().navigate(R.id.LoginFragment);
            return;
        }

        Log.i(TAG, "onActivityCreated")
        fab.setOnClickListener {
            Log.v(TAG, "creating new event")
            findNavController().navigate(R.id.EventEditFragment)
        }
        logout_btn.setOnClickListener{
            AuthRepository.logout()
            EventsRepository.clearEvents()
            findNavController().navigate(R.id.EventsListFragment)
        }
        setupEventsList()
    }

    private fun setupEventsList() {
        eventsListAdapter =
            EventsListAdapter(
                this
            )
        events_list.adapter = eventsListAdapter
        eventsModel = ViewModelProvider(this).get(EventsListViewModel::class.java)
        eventsModel.events.observe(viewLifecycleOwner,  Observer { value ->
            Log.i(TAG, "update items")
            eventsListAdapter.events = value
        })
        eventsModel.loading.observe(viewLifecycleOwner, Observer{loading->
            Log.i(TAG, "update loading")
            progress.visibility = if (loading) View.VISIBLE else View.GONE
        })
        eventsModel.loadingError.observe(viewLifecycleOwner, Observer { error->
            if(error!=null){
                Log.i(TAG, "update loading error")
                val message = "Loading exception ${error.message}"
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        })
        eventsModel.loadEvents()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
    }
}