package bigchris.studying.ledcubecontrol.ledbaselayer.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bigchris.studying.ledcubecontrol.R
import bigchris.studying.ledcubecontrol.ledbaselayer.FrameDummy

class FrameRecyclerviewAdapter(private val dataSet: ArrayList<FrameDummy>, private val frameSelectionListener: RecyclerViewSelectionListener)
        : RecyclerView.Adapter<FrameRecyclerviewAdapter.ViewHolder>() {
    var currentlySelected = 0

    interface RecyclerViewSelectionListener {

        fun onFrameSelected(position: Int)

        fun onFrameMarkedToDeletion(position: Int)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemPosition = -1
        val frameSelector: TextView = view.findViewById(R.id.frameSelectorTextView)

        init {
            frameSelector.setOnClickListener {
                notifyDataSetChanged()
                currentlySelected = this.layoutPosition
                notifyDataSetChanged()
                frameSelectionListener.onFrameSelected(itemPosition)
            }
            frameSelector.setOnLongClickListener {
                frameSelectionListener.onFrameMarkedToDeletion(itemPosition)
                true
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.frame_view_holder, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.frameSelector.setText(holder.itemView.context.resources.getString(R.string.frame_title,position))
        holder.itemPosition = position
        holder.itemView.isSelected = position == currentlySelected
    }


}