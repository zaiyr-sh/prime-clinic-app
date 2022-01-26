package kg.iaau.diploma.primeclinic.repository

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kg.iaau.diploma.data.About
import kg.iaau.diploma.primeclinic.databinding.ListItemAboutBinding

class AboutAdapter : ListAdapter<About, AboutViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutViewHolder {
        return AboutViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AboutViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<About> =
            object : DiffUtil.ItemCallback<About>() {
                override fun areItemsTheSame(oldItem: About, newItem: About) =
                    oldItem == newItem

                override fun areContentsTheSame(oldItem: About, newItem: About) =
                    oldItem.id == newItem.id
            }
    }

}

class AboutViewHolder (private val vb: ListItemAboutBinding) : RecyclerView.ViewHolder(vb.root) {

    fun bind(about: About) {
        vb.run {
            tvTitle.text = about.header
            tvDescription.text = about.paragraph
        }
    }

    companion object {
        fun from(parent: ViewGroup): AboutViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val vb = ListItemAboutBinding.inflate(layoutInflater, parent, false)
            return AboutViewHolder(vb)
        }
    }
}
