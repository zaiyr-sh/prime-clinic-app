package kg.iaau.diploma.primeclinic.ui.main.chat.calling

import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.ui.CoreActivity
import kg.iaau.diploma.core.utils.*
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.ActivityReceivingCallBinding
import kg.iaau.diploma.primeclinic.ui.main.chat.ChatVM

@AndroidEntryPoint
class ReceivingCallActivity : CoreActivity<ActivityReceivingCallBinding, ChatVM>(ChatVM::class.java) {

    override val bindingInflater: (LayoutInflater) -> ActivityReceivingCallBinding
        get() = ActivityReceivingCallBinding::inflate

    private lateinit var mp: MediaPlayer
    private lateinit var ref: DocumentReference

    private val userUid by lazy { intent.getStringExtra(USER_UID)!! }

    override fun setupActivityView() {
        playCallingSound()
        vb.run {
            FirebaseHelper.setupDoctorData(userUid) {
                val image = it.getString("image")
                val name = it.getString("name")
                val patronymic = it.getString("fatherName")
                ivUser.loadBase64Image(this@ReceivingCallActivity, image, R.drawable.ic_doctor)
                tvUsername.text = getString(R.string.name_with_patronymic, name, patronymic)
            }
        }
        setupActivityViewListeners()
    }

    private fun playCallingSound() {
        mp = MediaPlayer.create(this, R.raw.calling)
        mp.isLooping = true
        mp.start()
    }

    private fun setupActivityViewListeners() {
        ref = FirebaseFirestore.getInstance().collection("users").document(vm.userId.toString())
            .collection("call").document("calling")
        vb.run {
            givAccept.setOnSingleClickListener {
                val map = mutableMapOf<String, Boolean>().apply {
                    this["accepted"] = true
                }
                ref.set(map, SetOptions.merge()).addOnSuccessListener {
                    mp.stop()
                    finish()
                    VideoChatActivity.startActivity(this@ReceivingCallActivity, ref.path, tvUsername.text.toString())
                }
            }
            givCancel.setOnSingleClickListener {
                endCall()
            }
        }
        addCallListener()
    }

    private fun endCall() {
        val map = FirebaseHelper.getCallData("", "", accepted = false, declined = true)
        ref.set(map, SetOptions.merge()).addOnSuccessListener {
            mp.stop()
            ref.delete()
            finish()
        }
    }

    private fun addCallListener() {
        val ref = FirebaseFirestore.getInstance().collection("users").document(vm.userId.toString())
            .collection("call").document("calling")
        ref.addSnapshotListener { value, _ ->
            if (value?.getBoolean("declined") == true) {
                toast(getString(R.string.call_rejected))
                mp.stop()
                finish()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        endCall()
    }

    companion object {
        private const val USER_UID = "UID"
        fun startActivity(context: Context, userUid: String) {
            context.startActivity<ReceivingCallActivity> {
                putExtra(USER_UID, userUid)
            }
        }
    }

}