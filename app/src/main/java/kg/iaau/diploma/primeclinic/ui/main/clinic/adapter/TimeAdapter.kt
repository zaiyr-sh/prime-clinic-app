package kg.iaau.diploma.primeclinic.ui.main.clinic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kg.iaau.diploma.core.utils.isDrawableEqual
import kg.iaau.diploma.core.utils.toast
import kg.iaau.diploma.data.Slot
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.ListItemSlotBinding

class TimeAdapter(private var listener: TimeListener): ListAdapter<Slot, TimeViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        return TimeViewHolder.from(parent, listener)
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Slot> =
            object : DiffUtil.ItemCallback<Slot>() {
                override fun areItemsTheSame(oldItem: Slot, newItem: Slot) =
                    oldItem == newItem

                override fun areContentsTheSame(oldItem: Slot, newItem: Slot) =
                    oldItem.id == newItem.id
            }
    }

}

class TimeViewHolder(private val vb: ListItemSlotBinding) : RecyclerView.ViewHolder(vb.root) {

    private lateinit var slot: Slot

    fun bind(slot: Slot) {
        this.slot = slot
        vb.tvTime.text = itemView.resources.getString(
            R.string.time_period,
            slot.start?.substring(0, 5),
            slot.end?.substring(0,5)
        )
    }

    companion object {
        fun from(parent: ViewGroup, listener: TimeListener): TimeViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val vb = ListItemSlotBinding.inflate(layoutInflater, parent, false)
            return TimeViewHolder(vb).apply {
                itemView.run {
                    setOnClickListener {
                        if (vb.llSlot.isDrawableEqual(context, R.drawable.shape_free_slot)) {
                            if (listener.onTimeClick(slot)){
                                vb.llSlot.background = ContextCompat.getDrawable(context, R.drawable.shape_reserved_slot)
                                vb.tvTime.setTextColor(ContextCompat.getColor(context, R.color.white))
                            } else {
                                itemView.apply {
                                    context.toast(resources.getString(R.string.error_several_items_choosing))
                                }
                            }
                        } else {
                            listener.onTimeClick(null)
                            vb.llSlot.background = ContextCompat.getDrawable(context, R.drawable.shape_free_slot)
                            vb.tvTime.setTextColor(ContextCompat.getColor(context, R.color.teal))
                        }
                    }
                }
            }
        }
    }
}

interface TimeListener {
    fun onTimeClick(slot: Slot?): Boolean
}