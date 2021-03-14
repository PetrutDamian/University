package unique.damian.androidapp.core.eventList

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_event.view.*
import unique.damian.androidapp.core.data.Event
import unique.damian.androidapp.core.event.EventEditFragment
import unique.damian.androidapp.Others.TAG
import unique.damian.androidapp.R
import unique.damian.androidapp.core.data.OfflineStatus

class EventsListAdapter(
    private val fragment:Fragment
): RecyclerView.Adapter<EventsListAdapter.ViewHolder>(){
    var events = emptyList<Event>()
        set(value) {
            field = value
            notifyDataSetChanged();
        }

    private var onEventClick: View.OnClickListener

    init {
        onEventClick = View.OnClickListener { view ->
            val event = view.tag as Event
            fragment.findNavController().navigate(R.id.EventEditFragment, Bundle().apply {
                putString(EventEditFragment.EVENT_ID, event._id)
            })
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.text_title
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_event, parent, false)
        Log.v(TAG, "onCreateViewHolder")
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = events.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.v(TAG, "onBindViewHolder $position")
        val event = events[position]
        holder.textView.text = event.title
        holder.itemView.tag = event
        if(event.offline!=OfflineStatus.DELETED)
            holder.itemView.setOnClickListener(onEventClick)
        if(event.offline!=null)
            holder.itemView.setBackgroundColor(Color.RED)
        else
            holder.itemView.setBackgroundColor(Color.WHITE)
    }
}