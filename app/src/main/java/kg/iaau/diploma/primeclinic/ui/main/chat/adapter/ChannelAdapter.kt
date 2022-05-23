package kg.iaau.diploma.primeclinic.ui.main.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kg.iaau.diploma.core.utils.*
import kg.iaau.diploma.data.Chat
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.ListItemChannelBinding
import java.util.*

class ChannelAdapter(options: FirestoreRecyclerOptions<Chat>, private var listener: ChannelListener) :
    FirestoreRecyclerAdapter<Chat, ChannelViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        return ChannelViewHolder.from(parent, listener)
    }

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int, model: Chat) {
        val item = getItem(position)
        holder.bind(item)
    }

}

class ChannelViewHolder(private val vb: ListItemChannelBinding) : RecyclerView.ViewHolder(vb.root) {

    fun bind(chat: Chat) {
        vb.run {
            tvTime.text = chat.lastMessageTime?.toDate()?.formatForDate()
            tvMessage.text = when (chat.lastMessageSenderId) {
                chat.adminId -> itemView.context.getString(R.string.doctor_message, getMessage(chat))
                chat.clientId -> itemView.context.getString(R.string.your_message, getMessage(chat))
                else -> ""
            }
            setupUser(chat.adminId)
            setupChannelEnabling(chat)
        }
    }

    private fun setupChannelEnabling(chat: Chat) {
        val chatStartedDate = chat.chatTime?.toDate()
        val currentDate = Date()
        when(chatStartedDate == null) {
            true -> vb.clContainer.setVisible(false)
            else -> vb.clContainer.setVisible(currentDate.remainFromInDays(chatStartedDate) == 0L)
        }
    }

    private fun setChatEnabled(isEnabled: Boolean) {
        vb.run {
            when (isEnabled) {
                true -> clContainer.setEnable(true)
                false -> {
                    tvMessage.text = itemView.context.getString(R.string.chat_not_available)
                    tvMessage.setTextColor(itemView.context.setColor(R.color.red))
                    clContainer.setEnable(false)
                }
            }
        }
    }

    private fun getMessage(chat: Chat): String? {
        return chat.lastMessage?.let { message ->
            if(message.length > 15) message.take(15) + "..."
            else message
        }
    }

    private fun setupUser(adminId: String?) {
        val db = FirebaseFirestore.getInstance()
        vb.run {
            adminId?.let { id ->
                db.collection("doctors").document(id).get().addOnSuccessListener {
                    val image = it.getString("image")
                    val name = it.getString("name") ?: ""
                    val fatherName = it.getString("fatherName") ?: ""
                    ivProfile.loadBase64Image(itemView.context, image, R.drawable.ic_doctor)
                    val fullName = "$name $fatherName"
                    tvName.text = when (fullName.isFullyEmpty()) {
                        true -> itemView.context.getString(R.string.name_not_set)
                        else -> fullName
                    }
                }
            }
        }
    }

    companion object {
        fun from(parent: ViewGroup, listener: ChannelListener): ChannelViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val vb = ListItemChannelBinding.inflate(layoutInflater, parent, false)
            return ChannelViewHolder(vb).apply {
                itemView.setOnClickListener {
                    listener.onChannelClick(bindingAdapterPosition)
                }
            }
        }
    }
}

interface ChannelListener {
    fun onChannelClick(position: Int)
}