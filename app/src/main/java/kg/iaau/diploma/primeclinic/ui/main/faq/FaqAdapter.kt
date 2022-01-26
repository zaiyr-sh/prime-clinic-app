package kg.iaau.diploma.primeclinic.ui.main.faq

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kg.iaau.diploma.core.utils.gone
import kg.iaau.diploma.core.utils.show
import kg.iaau.diploma.data.Faq
import kg.iaau.diploma.primeclinic.databinding.ListItemFaqBinding

class FaqAdapter : ListAdapter<Faq, FaqViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        return FaqViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Faq> =
            object : DiffUtil.ItemCallback<Faq>() {
                override fun areItemsTheSame(oldItem: Faq, newItem: Faq) =
                    oldItem == newItem

                override fun areContentsTheSame(oldItem: Faq, newItem: Faq) =
                    oldItem.id == newItem.id
            }
    }

}

class FaqViewHolder(private val vb: ListItemFaqBinding) : RecyclerView.ViewHolder(vb.root) {

    fun bind(faq: Faq) {
        vb.run {
            tvQuestion.text = faq.question
            tvAnswer.text = faq.answer
        }
    }

    companion object {
        fun from(parent: ViewGroup): FaqViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val vb = ListItemFaqBinding.inflate(layoutInflater, parent, false)
            return FaqViewHolder(vb).apply {
                vb.run {
                    clFaq.setOnClickListener {
                        if(tvAnswer.isVisible)
                            tvAnswer.gone()
                        else
                            tvAnswer.show()

                    }
                }
            }
        }
    }
}