package kg.iaau.diploma.primeclinic.ui.main.clinic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kg.iaau.diploma.core.utils.convertBase64ToDrawable
import kg.iaau.diploma.data.Doctor
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.ListItemDoctorBinding

class DoctorAdapter(private var listener: DoctorListener) : ListAdapter<Doctor, DoctorViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        return DoctorViewHolder.from(parent, listener)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Doctor> =
            object : DiffUtil.ItemCallback<Doctor>() {
                override fun areItemsTheSame(oldItem: Doctor, newItem: Doctor) =
                    oldItem == newItem

                override fun areContentsTheSame(oldItem: Doctor, newItem: Doctor) =
                    oldItem.id == newItem.id
            }
    }

}

class DoctorViewHolder(private val vb: ListItemDoctorBinding) : RecyclerView.ViewHolder(vb.root) {

    private lateinit var doctor: Doctor

    fun bind(doctor: Doctor) {
        this.doctor = doctor
        vb.run {
            tvName.text = itemView.resources.getString(R.string.full_name, doctor.lastName, doctor.firstName, doctor.patronymic)
            tvExperience.text = doctor.position
            if (doctor.image.isNullOrEmpty())
                ivProfile.setImageDrawable(itemView.resources.getDrawable(R.drawable.shape_filled_dot, itemView.context.theme))
            else
                ivProfile.setImageDrawable(doctor.image?.convertBase64ToDrawable(itemView.context))
        }
    }

    companion object {
        fun from(parent: ViewGroup, listener: DoctorListener): DoctorViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val vb = ListItemDoctorBinding.inflate(layoutInflater, parent, false)
            return DoctorViewHolder(vb).apply {
                itemView.setOnClickListener {
                    listener.onDoctorClick(doctor.id)
                }
            }
        }
    }
}

interface DoctorListener {
    fun onDoctorClick(id: Long?)
}