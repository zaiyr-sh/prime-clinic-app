package kg.iaau.diploma.primeclinic.ui.main.chat.calling

import android.Manifest
import android.content.Context
import android.content.Intent
import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.opentok.android.*
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.utils.startActivity
import kg.iaau.diploma.core.utils.toast
import kg.iaau.diploma.primeclinic.MainActivity
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.ActivityVideoChatBinding
import kg.iaau.diploma.primeclinic.ui.main.med_card.bottom_sheet.ProfilePictureBottomSheetFragment

@AndroidEntryPoint
class VideoChatActivity : AppCompatActivity(), Session.SessionListener,
    PublisherKit.PublisherListener {

    private lateinit var vb: ActivityVideoChatBinding
    private val refPath by lazy { intent.getStringExtra(REF)!! }
    private val username by lazy { intent.getStringExtra(USERNAME)!! }

    private lateinit var ref: DocumentReference
    private lateinit var mSession: Session
    private var mPublisher: Publisher? = null
    private var mSubscriber: Subscriber? = null

    private var requestPermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        permissions.entries.forEach {
            if (!it.value) {
                mSession = Session.Builder(this, API_KEY, SESSION_ID).build()
                mSession.setSessionListener(this)
                mSession.connect(TOKEN)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityVideoChatBinding.inflate(layoutInflater)
        setContentView(vb.root)
        setupActivityView()
        requestPermissions.launch(permissions)
    }

    private fun setupActivityView() {
        ref = FirebaseFirestore.getInstance().document(refPath)
        addSnapshotListener()
        vb.run {
            tvUsername.text = username
            givCancel.setOnClickListener {
                endCall()
            }
        }
    }

    private fun addSnapshotListener() {
        ref.addSnapshotListener { value, _ ->
            if (value != null && value.exists()) {
                val uid = value.getString("uid")
                if (uid.isNullOrEmpty()) {
                    goBack()
                }
            }
        }
    }

    private fun endCall() {
        val map = mutableMapOf<String, Any>()
        map["accepted"] = false
        map["declined"] = false
        map["uid"] = ""
        map["receiverId"] = ""
        ref.set(map, SetOptions.merge()).addOnSuccessListener {
            goBack()
        }
    }

    private fun goBack() {
        mSubscriber?.destroy()
        mPublisher?.destroy()
        toast(getString(R.string.call_finished))
        MainActivity.startActivity(this)
        finish()
    }

    override fun onConnected(p0: Session?) {
        Log.d("VideoChatActivity", "onConnected(): ")
        mPublisher = Publisher.Builder(this).build()
        mPublisher?.setPublisherListener(this)
        vb.flDoctor.addView(mPublisher?.view)
        if (mPublisher?.view is GLSurfaceView) {
            (mPublisher?.view as GLSurfaceView).setZOrderOnTop(true)
        }
        mSession.publish(mPublisher)
    }

    override fun onDisconnected(p0: Session?) {
        Log.d("VideoChatActivity", "onDisconnected(): ")
    }

    override fun onStreamReceived(p0: Session?, p1: Stream?) {
        Log.d("VideoChatActivity", "onStreamReceived(): ")
        if (mSubscriber == null) {
            mSubscriber = Subscriber.Builder(this, p1!!).build()
            mSession.subscribe(mSubscriber)
            vb.flContainer.addView(mSubscriber!!.view)
        }
    }

    override fun onStreamDropped(p0: Session?, p1: Stream?) {
        Log.d("VideoChatActivity", "onStreamDropped (): ")
        if (mSubscriber != null) {
            mSubscriber = null
            vb.flContainer.removeAllViews()
        }
    }

    override fun onError(p0: Session?, p1: OpentokError?) {
        Log.d("VideoChatActivity", "onError(): $p1")
    }

    override fun onStreamCreated(p0: PublisherKit?, p1: Stream?) {
        Log.d("VideoChatActivity", "onStreamCreated(): ")
    }

    override fun onStreamDestroyed(p0: PublisherKit?, p1: Stream?) {
        Log.d("VideoChatActivity", "onStreamDestroyed(): ")
    }

    override fun onError(p0: PublisherKit?, p1: OpentokError?) {
        Log.d("VideoChatActivity", "onError(): ")
    }

    companion object {
        private const val API_KEY = "46923824"
        private var SESSION_ID =
            "2_MX40NjkyMzgyNH5-MTYwMDMyODU0NTY0OH5INDhFcHlIcjEwbGF6MEk0ZTV6SHRKRzB-fg"
        private var TOKEN =
            "T1==cGFydG5lcl9pZD00NjkyMzgyNCZzaWc9N2YwMjljNjc5YjE5YjIyZWEwN2FmMTBhYmYwNWEwZjliOGMyZDVkNTpzZXNzaW9uX2lkPTJfTVg0ME5qa3lNemd5Tkg1LU1UWXdNRE15T0RVME5UWTBPSDVJTkRoRmNIbEljakV3YkdGNk1FazBaVFY2U0hSS1J6Qi1mZyZjcmVhdGVfdGltZT0xNjAwMzI4NjI2Jm5vbmNlPTAuMjIzNzU2MDQ0NjYwODE0Njcmcm9sZT1wdWJsaXNoZXImZXhwaXJlX3RpbWU9MTYwMjkyMDYyNSZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ=="
        private const val RC_VIDEO_APP_PERM = 124

        private val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
        )

        private const val REF = "ref"
        private const val USERNAME = "username"
        fun startActivity(context: Context, ref: String, username: String) {
            context.startActivity<VideoChatActivity> {
                putExtra(REF, ref)
                putExtra(USERNAME, username)
            }
        }
    }

}