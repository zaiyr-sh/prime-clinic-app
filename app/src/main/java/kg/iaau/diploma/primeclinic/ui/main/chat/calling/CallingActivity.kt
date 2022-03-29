package kg.iaau.diploma.primeclinic.ui.main.chat.calling

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.utils.startActivity
import kg.iaau.diploma.core.utils.toast
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.ActivityCallingBinding
import kg.iaau.diploma.primeclinic.ui.main.chat.ChatVM

@AndroidEntryPoint
class CallingActivity : AppCompatActivity() {

    private lateinit var vb: ActivityCallingBinding
    private val userId by lazy { intent.getStringExtra(USER_ID)!! }
    private var listener: ListenerRegistration? = null
    private val vm: ChatVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityCallingBinding.inflate(layoutInflater)
        setContentView(vb.root)
        setupActivityView()
    }

    private fun setupActivityView() {
        vb.run {
            val db = FirebaseFirestore.getInstance()
            db.collection("doctors").document(userId).get().addOnSuccessListener {
                val image = it.getString("image")
                val name = it.getString("name")
                val patronymic = it.getString("fatherName")
                if (!image.isNullOrEmpty())
                    Glide.with(this@CallingActivity).load(image).into(ivUser)
                tvUsername.text = getString(R.string.name_with_patronymic, name, patronymic)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null)
            makePhoneCall()
    }

    private fun getCallData(
        uid: String,
        receiverId: String,
        accepted: Boolean,
        declined: Boolean
    ): MutableMap<String, Any> {
        val callData = mutableMapOf<String, Any>()
        callData["uid"] = uid
        callData["receiverId"] = receiverId
        callData["accepted"] = accepted
        callData["declined"] = declined
        return callData
    }

    private fun makePhoneCall() {
        val db = FirebaseFirestore.getInstance()
        val ref =
            db.collection("doctors").document(userId).collection("call").document("calling")
        ref.get().addOnSuccessListener {
            when(it.exists() && !it.getString("uid").isNullOrEmpty()) {
                true -> {
                    toast(getString(R.string.doctor_not_available))
                    setEndCall(ref)
                }
                else -> {
                    val callData = getCallData(vm.userId.toString(), userId, accepted = false, declined = false)
                    ref.set(callData).addOnSuccessListener {
                        addSnapListener(ref)
                        setEndCall(ref)
                    }
                }
            }
        }
    }

    private fun setEndCall(ref: DocumentReference) {
        vb.givCancel.setOnClickListener {
            val callData = getCallData("", "", accepted = false, declined = true)
            ref.set(callData).addOnSuccessListener {
                toast(getString(R.string.call_finished))
                finish()
            }
        }
    }

    private fun addSnapListener(ref: DocumentReference) {
        listener = ref.addSnapshotListener { value, _ ->
            if (value != null && value.exists()) {
                val accepted = value.getBoolean("accepted")
                val declined = value.getBoolean("declined")
                if (accepted == true) {
                    toast(getString(R.string.call_accepted))
                    VideoChatActivity.startActivity(this, ref.path, vb.tvUsername.text.toString())
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    finish()
                    startActivity(intent)
                }
                if (declined != null && declined == true) {
                    toast(getString(R.string.call_rejected))
                    finish()
                }

            }

        }
    }

    companion object {
        private const val USER_ID = "USER_ID"
        fun startActivity(context: Context, userId: String) {
            context.startActivity<CallingActivity> {
                putExtra(USER_ID, userId)
            }
        }
    }

}