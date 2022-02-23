package kg.iaau.diploma.primeclinic.ui.main.clinic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kg.iaau.diploma.core.utils.convertBase64ToBitmap
import kg.iaau.diploma.data.Payment
import kg.iaau.diploma.primeclinic.databinding.ListItemPaymentBinding

class PaymentAdapter(private var listener: PaymentListener): ListAdapter<Payment, PaymentViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        return PaymentViewHolder.from(parent, listener)
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Payment> =
            object : DiffUtil.ItemCallback<Payment>() {
                override fun areItemsTheSame(oldItem: Payment, newItem: Payment) =
                    oldItem == newItem

                override fun areContentsTheSame(oldItem: Payment, newItem: Payment) =
                    oldItem.id == newItem.id
            }
    }

}

class PaymentViewHolder(private val vb: ListItemPaymentBinding) : RecyclerView.ViewHolder(vb.root) {

    private lateinit var payment: Payment

    fun bind(payment: Payment) {
        this.payment = payment
        vb.ivPayLogo.setImageBitmap(payment.logo?.convertBase64ToBitmap())
    }

    companion object {
        fun from(parent: ViewGroup, listener: PaymentListener): PaymentViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val vb = ListItemPaymentBinding.inflate(layoutInflater, parent, false)
            return PaymentViewHolder(vb).apply {
                itemView.run {
                    setOnClickListener {
                        listener.onPaymentClick(payment)
                    }
                }
            }
        }
    }
}

interface PaymentListener {
    fun onPaymentClick(payment: Payment)
}