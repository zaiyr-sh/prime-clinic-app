package kg.iaau.diploma.primeclinic.ui.main.clinic.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kg.iaau.diploma.data.SpecialistCategory
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.ListItemSpecialistsBinding

class ClinicSpecialistAdapter(private var listener: ClinicSpecialistListener) : ListAdapter<SpecialistCategory, ClinicSpecialistViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicSpecialistViewHolder {
        return ClinicSpecialistViewHolder.from(parent, listener)
    }

    override fun onBindViewHolder(holder: ClinicSpecialistViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<SpecialistCategory> =
            object : DiffUtil.ItemCallback<SpecialistCategory>() {
                override fun areItemsTheSame(oldItem: SpecialistCategory, newItem: SpecialistCategory) =
                    oldItem == newItem

                override fun areContentsTheSame(oldItem: SpecialistCategory, newItem: SpecialistCategory) =
                    oldItem.id == newItem.id
            }
    }

}
class ClinicSpecialistViewHolder(private val vb: ListItemSpecialistsBinding) : RecyclerView.ViewHolder(vb.root) {

    private lateinit var specialist: SpecialistCategory

    @SuppressLint("ResourceAsColor")
    fun bind(specialist: SpecialistCategory) {
        this.specialist = specialist
        vb.run {
            tvDescription.text = specialist.description
            tvTitle.text = specialist.name
            if (specialist.image.isNullOrEmpty())
                ivSpecialist.setBackgroundColor(R.color.teal)
            else
                Glide.with(itemView.context).load(specialist.image).into(ivSpecialist)
        }
    }

    companion object {
        fun from(parent: ViewGroup, listener: ClinicSpecialistListener): ClinicSpecialistViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val vb = ListItemSpecialistsBinding.inflate(layoutInflater, parent, false)
            return ClinicSpecialistViewHolder(vb).apply {
                itemView.setOnClickListener {
                    listener.onSpecialistClick(specialist.id)
                }
            }
        }
    }
}

interface ClinicSpecialistListener {
    fun onSpecialistClick(id: Long?)
}