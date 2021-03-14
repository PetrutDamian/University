package unique.damian.androidapp.core.event

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_event_edit.*
import unique.damian.androidapp.core.data.Event
import unique.damian.androidapp.Others.TAG
import unique.damian.androidapp.R

class EventEditFragment:Fragment(){
    companion object {
        const val EVENT_ID = "EVENT_ID"
    }
    private lateinit var viewModel: EventEditViewModel
    private var eventId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate")
        arguments?.let {
            if (it.containsKey(EVENT_ID)) {
                Log.v(TAG,"received bundle id:"+it.getString(EVENT_ID));
                eventId = it.getString(EVENT_ID)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_event_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v(TAG, "onViewCreated")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
        setupViewModel()
        if(eventId == null)
            button_delete.isVisible = false
        else
            button_delete.setOnClickListener {
                viewModel.deleteEvent(eventId!!)
            }
        button_save.setOnClickListener{
            Log.v(TAG, "save event")
            var idString = ""
            if(eventId!=null){
                idString = eventId as String
            }
            Log.v(TAG,"is reservation checked?:${checkbox_reservation.isChecked}")
            viewModel.saveEvent(
                Event(
                    idString,
                    input_title.text.toString(),
                    input_description.text.toString(),
                    input_location.text.toString(),
                    checkbox_reservation.isChecked,
                    input_price.text.toString().toInt()
                )
            )
        }

    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(EventEditViewModel::class.java)
        viewModel.event.observe(viewLifecycleOwner, Observer{ event ->
            Log.v(TAG, "update events")
            if(event!=null){
                input_title.setText( event.title)
                input_description.setText(event.description)
                input_location.setText(event.location)
                input_price.setText(event.price.toString())
                checkbox_reservation.isChecked = event.reservationRequired
            }
        })

        viewModel.fetching.observe(viewLifecycleOwner, Observer{ fetching ->
            Log.v(TAG, "update fetching")
            progress2.visibility = if (fetching) View.VISIBLE else View.GONE
        })
        viewModel.fetchingError.observe(viewLifecycleOwner, Observer{ exception ->
            if (exception != null) {
                Log.v(TAG, "update fetching error")
                val message = "Fetching exception ${exception.message}"
                val parentActivity = activity?.parent
                if (parentActivity != null) {
                    Toast.makeText(parentActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.completed.observe(viewLifecycleOwner, Observer { completed ->
            if (completed) {
                Log.v(TAG, "completed, navigate back")
                findNavController().navigateUp()
            }
        })
        val id = eventId
        if (id != null) {
            viewModel.loadEvent(id)
        }
    }

}