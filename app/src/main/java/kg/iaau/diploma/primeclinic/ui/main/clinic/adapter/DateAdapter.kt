package kg.iaau.diploma.primeclinic.ui.main.clinic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kg.iaau.diploma.core.utils.formatForCurrentDate
import kg.iaau.diploma.core.utils.isDrawableEqual
import kg.iaau.diploma.core.utils.toast
import kg.iaau.diploma.data.Interval
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.ListItemSlotBinding

class DateAdapter(private var listener: DateListener): ListAdapter<Interval, DateViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        return DateViewHolder.from(parent, listener)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
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

class DateViewHolder(private val vb: ListItemSlotBinding) : RecyclerView.ViewHolder(vb.root) {

    private lateinit var interval: Interval

    fun bind(interval: Interval) {
        this.interval = interval
        vb.tvTime.text = interval.start?.formatForCurrentDate()
    }

    companion object {
        fun from(parent: ViewGroup, listener: DateListener): DateViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val vb = ListItemSlotBinding.inflate(layoutInflater, parent, false)
            return DateViewHolder(vb).apply {
                itemView.run {
                    setOnClickListener {
                        if (vb.llSlot.isDrawableEqual(context, R.drawable.shape_free_slot)) {
                            if (listener.onDateClick(interval)){
                                vb.llSlot.background = ContextCompat.getDrawable(context, R.drawable.shape_reserved_slot)
                                vb.tvTime.setTextColor(ContextCompat.getColor(context, R.color.white))
                            } else {
                                itemView.apply {
                                    context.toast(resources.getString(R.string.error_several_items_choosing))
                                }
                            }
                        } else {
                            listener.onDateClick(null)
                            vb.llSlot.background = ContextCompat.getDrawable(context, R.drawable.shape_free_slot)
                            vb.tvTime.setTextColor(ContextCompat.getColor(context, R.color.teal))
                        }
                    }
                }
            }
        }
    }
}

interface DateListener {
    fun onDateClick(interval: Interval?): Boolean
}