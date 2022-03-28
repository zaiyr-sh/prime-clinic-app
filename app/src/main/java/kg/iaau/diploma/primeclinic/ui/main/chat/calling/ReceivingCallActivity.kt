package kg.iaau.diploma.primeclinic.ui.main.chat.calling

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.utils.startActivity
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.ActivityReceivingCallBinding
import kg.iaau.diploma.primeclinic.ui.main.chat.ChatVM

@AndroidEntryPoint
class ReceivingCallActivity : AppCompatActivity() {

    private lateinit var vb: ActivityReceivingCallBinding
    private val userUid by lazy { intent.getStringExtra(USER_UID)!! }
    private val vm: ChatVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityReceivingCallBinding.inflate(layoutInflater)
        setContentView(vb.root)
        setupActivityView()
        setupActivityViewListeners()
    }

    private fun setupActivityView() {
        vb.run {
            val ref = FirebaseFirestore.getInstance().collection("doctors").document(userUid)
            ref.get().addOnSuccessListener {
                val image = it.getString("image")
                val name = it.getString("name")
                val patronymic = it.getString("fatherName")
                if (!image.isNullOrEmpty())
                    Glide.with(this@ReceivingCallActivity).load(image).into(ivUser)
                tvUsername.text = getString(R.string.name_with_patronymic, name, patronymic)
            }
        }
    }

    private fun setupActivityViewListeners() {
        val ref = FirebaseFirestore.getInstance().collection("users").document(vm.userId.toString())
            .collection("call").document("calling")
        vb.run {
            givAccept.setOnClickListener {
                val map = mutableMapOf<String, Boolean>()
                map["accepted"] = true
                ref.set(map, SetOptions.merge()).addOnSuccessListener {
                    VideoChatActivity.startActivity(this@ReceivingCallActivity, ref.path, tvUsername.text.toString())
                    finish()
                }
            }
            givCancel.setOnClickListener {
                val map = mutableMapOf<String, Any>()
                map["accepted"] = false
                map["declined"] = true
                map["uid"] = ""
                map["receiverId"] = ""
                ref.set(map, SetOptions.merge()).addOnSuccessListener {
                    onBackPressed()
                }
            }
        }
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