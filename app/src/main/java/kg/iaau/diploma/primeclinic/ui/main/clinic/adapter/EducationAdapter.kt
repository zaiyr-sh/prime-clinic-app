package kg.iaau.diploma.primeclinic.ui.main.clinic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kg.iaau.diploma.data.Education
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.ListItemEducationBinding

class EducationAdapter : ListAdapter<Education, EducationViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EducationViewHolder {
        return EducationViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: EducationViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Education> =
            object : DiffUtil.ItemCallback<Education>() {
                override fun areItemsTheSame(oldItem: Education, newItem: Education) =
                    oldItem == newItem
                override fun areContentsTheSame(oldItem: Education, newItem: Education) =
                    oldItem.start == newItem.start
            }
    }

}

class EducationViewHolder(private val vb: ListItemEducationBinding) : RecyclerView.ViewHolder(vb.root) {

    fun bind(education: Education) {
        vb.run {
            tvYear.text = itemView.resources.getString(
                R.string.education_period,
                education.start,
                education.end
            )
            tvInfo.text = itemView.resources.getString(
                R.string.education_information,
                education.name,
                education.organizationName
            )
        }
    }

    companion object {
        fun from(parent: ViewGroup): EducationViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val vb = ListItemEducationBinding.inflate(layoutInflater, parent, false)
            return EducationViewHolder(vb)
        }
    }
}