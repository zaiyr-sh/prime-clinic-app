package kg.iaau.diploma.primeclinic.ui.main.chat.calling

import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.ui.CoreActivity
import kg.iaau.diploma.core.utils.FirebaseHelper
import kg.iaau.diploma.core.utils.loadWithFresco
import kg.iaau.diploma.core.utils.startActivity
import kg.iaau.diploma.core.utils.toast
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.ActivityCallingBinding
import kg.iaau.diploma.primeclinic.ui.main.chat.ChatVM

@AndroidEntryPoint
class CallingActivity : CoreActivity<ActivityCallingBinding, ChatVM>(ChatVM::class.java) {

    override val bindingInflater: (LayoutInflater) -> ActivityCallingBinding
        get() = ActivityCallingBinding::inflate

    private lateinit var mp: MediaPlayer

    private val userId by lazy { intent.getStringExtra(USER_ID)!! }
    private var listener: ListenerRegistration? = null

    override fun setupActivityView() {
        playPhoneBeepSound()
        vb.run {
            FirebaseHelper.setupDoctorData(userId) {
                val image = it.getString("image")
                val name = it.getString("name")
                val patronymic = it.getString("fatherName")
                ivUser.loadWithFresco(image, onFail = {
                    ivUser.setActualImageResource(R.drawable.shape_filled_dot)
                })
                tvUsername.text = getString(R.string.name_with_patronymic, name, patronymic)
            }
        }
        makePhoneCall()
    }

    private fun playPhoneBeepSound() {
        mp = MediaPlayer.create(this, R.raw.phone_beep)
        mp.isLooping = true
        mp.start()
    }

    private fun makePhoneCall() {
        FirebaseHelper.makeCall(userId,
            onSuccess = { ref ->
                val callData = FirebaseHelper.getCallData(vm.userId.toString(), userId, accepted = false, declined = false)
                ref.set(callData).addOnSuccessListener {
                    addSnapListener(ref)
                    setEndCall(ref)
                }
            },
            onFail = {
                toast(getString(R.string.doctor_not_available))
                mp.stop()
                finish()
            }
        )
    }

    private fun setEndCall(ref: DocumentReference) {
        vb.givCancel.setOnClickListener {
            val callData = FirebaseHelper.getCallData("", "", accepted = false, declined = true)
            ref.set(callData).addOnSuccessListener {
                toast(getString(R.string.call_finished))
                finish()
            }
        }
    }

    private fun addSnapListener(ref: DocumentReference) {
        listener = FirebaseHelper.addCallAcceptanceListener(
            ref,
            onSuccess = {
                toast(getString(R.string.call_accepted))
                VideoChatActivity.startActivity(this, ref.path, vb.tvUsername.text.toString())
                mp.stop()
                finish()
            },
            onFail = {
                toast(getString(R.string.call_rejected))
                mp.stop()
                finish()
            }
        )
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