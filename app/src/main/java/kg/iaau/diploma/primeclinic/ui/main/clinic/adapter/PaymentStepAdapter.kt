package kg.iaau.diploma.primeclinic.ui.main.clinic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kg.iaau.diploma.data.PaymentStep
import kg.iaau.diploma.primeclinic.databinding.ListItemPayStepBinding

class PaymentStepAdapter : ListAdapter<PaymentStep, PaymentStepViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentStepViewHolder {
        return PaymentStepViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PaymentStepViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<PaymentStep> =
            object : DiffUtil.ItemCallback<PaymentStep>() {
                override fun areItemsTheSame(oldItem: PaymentStep, newItem: PaymentStep) =
                    oldItem == newItem

                override fun areContentsTheSame(oldItem: PaymentStep, newItem: PaymentStep) =
                    oldItem.id == newItem.id
            }
    }

}

class PaymentStepViewHolder(private val vb: ListItemPayStepBinding) : RecyclerView.ViewHolder(vb.root) {

    fun bind(step: PaymentStep) {
        vb.run {
            tvNumber.text = step.number.toString()
            tvStep.text = step.text
        }
    }

    companion object {
        fun from(parent: ViewGroup): PaymentStepViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val vb = ListItemPayStepBinding.inflate(layoutInflater, parent, false)
            return PaymentStepViewHolder(vb)
        }
    }
}
