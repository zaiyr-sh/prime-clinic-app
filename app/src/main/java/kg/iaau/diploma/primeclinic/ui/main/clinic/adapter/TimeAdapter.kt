package kg.iaau.diploma.primeclinic.ui.main.clinic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kg.iaau.diploma.data.Slot
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
        vb.tvTime.text = slot.start?.substring(0, 5).plus("-").plus(slot.end?.substring(0,5))
    }

    companion object {
        fun from(parent: ViewGroup, listener: TimeListener): TimeViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val vb = ListItemSlotBinding.inflate(layoutInflater, parent, false)
            return TimeViewHolder(vb).apply {
                itemView.run {
                    setOnClickListener {
                        listener.onTimeClick(slot)
                    }
                }
            }
        }
    }
}

interface TimeListener {
    fun onTimeClick(slot: Slot)
}