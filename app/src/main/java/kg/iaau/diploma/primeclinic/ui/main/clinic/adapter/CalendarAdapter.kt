package kg.iaau.diploma.primeclinic.ui.main.clinic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kg.iaau.diploma.data.Interval
import kg.iaau.diploma.primeclinic.databinding.ListItemSlotBinding

class CalendarAdapter(private var listener: CalendarListener): ListAdapter<Interval, CalendarViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        return CalendarViewHolder.from(parent, listener)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Interval> =
            object : DiffUtil.ItemCallback<Interval>() {
                override fun areItemsTheSame(oldItem: Interval, newItem: Interval) =
                    oldItem == newItem

                override fun areContentsTheSame(oldItem: Interval, newItem: Interval) =
                    oldItem.id == newItem.id
            }
    }

}

class CalendarViewHolder(private val vb: ListItemSlotBinding) : RecyclerView.ViewHolder(vb.root) {

    private lateinit var interval: Interval

    fun bind(interval: Interval) {
        this.interval = interval
        vb.tvTime.text = interval.start?.substring(0, 10)?.replace('-','.')
    }

    companion object {
        fun from(parent: ViewGroup, listener: CalendarListener): CalendarViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val vb = ListItemSlotBinding.inflate(layoutInflater, parent, false)
            return CalendarViewHolder(vb).apply {
                itemView.setOnClickListener {
                    listener.onIntervalClick(interval)
                }
            }
        }
    }
}

interface CalendarListener {
    fun onIntervalClick(interval: Interval)
}