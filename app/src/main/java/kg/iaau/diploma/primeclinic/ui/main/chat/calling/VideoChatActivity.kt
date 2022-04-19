package kg.iaau.diploma.primeclinic.ui.main.chat.calling

import android.Manifest
import android.content.Context
import android.media.MediaPlayer
import android.opengl.GLSurfaceView
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.opentok.android.*
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.ui.BaseActivity
import kg.iaau.diploma.core.utils.FirebaseHelper
import kg.iaau.diploma.core.utils.startActivity
import kg.iaau.diploma.core.utils.toast
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.ActivityVideoChatBinding

@AndroidEntryPoint
class VideoChatActivity : BaseActivity<ActivityVideoChatBinding>(), Session.SessionListener,
    PublisherKit.PublisherListener {

    override val bindingInflater: (LayoutInflater) -> ActivityVideoChatBinding
        get() = ActivityVideoChatBinding::inflate

    private lateinit var mp: MediaPlayer

    private val refPath by lazy { intent.getStringExtra(REF)!! }
    private val username by lazy { intent.getStringExtra(USERNAME)!! }

    private lateinit var ref: DocumentReference
    private lateinit var mSession: Session
    private var mPublisher: Publisher? = null
    private var mSubscriber: Subscriber? = null

    private var requestPermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val allPermitted = permissions.entries.all { it.value }
        if (allPermitted) {
            mSession = Session.Builder(this, API_KEY, SESSION_ID).build()
            mSession.setSessionListener(this)
            mSession.connect(TOKEN)
            playConnectingSound()
        } else finish()
    }

    private fun playConnectingSound() {
        mp = MediaPlayer.create(this, R.raw.connecting)
        mp.isLooping = true
        mp.start()
    }

    override fun setupActivityView() {
        requestPermissions.launch(permissions)
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
        val map = FirebaseHelper.getCallData("", "", accepted = false, declined = false)
        ref.set(map, SetOptions.merge()).addOnSuccessListener {
            goBack()
        }
    }

    private fun goBack() {
        mSubscriber?.destroy()
        mPublisher?.destroy()
        toast(getString(R.string.call_finished))
        mp.stop()
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
        goBack()
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
            mp.stop()
            goBack()
        }
    }

    override fun onError(p0: Session?, p1: OpentokError?) {
        Log.d("VideoChatActivity", "onError(): $p1")
        mp.stop()
        goBack()
    }

    override fun onStreamCreated(p0: PublisherKit?, p1: Stream?) {
        Log.d("VideoChatActivity", "onStreamCreated(): ")
        mp.stop()
    }

    override fun onStreamDestroyed(p0: PublisherKit?, p1: Stream?) {
        Log.d("VideoChatActivity", "onStreamDestroyed(): ")
        mp.stop()
        goBack()
    }

    override fun onError(p0: PublisherKit?, p1: OpentokError?) {
        Log.d("VideoChatActivity", "onError(): ")
        mp.stop()
        goBack()
    }

    override fun onDestroy() {
        super.onDestroy()
        goBack()
    }

    companion object {
        private const val API_KEY = "47483661"
        private var SESSION_ID =
            "1_MX40NzQ4MzY2MX5-MTY0OTg2OTY0MzU3Nn55TVFDVmhpZHVvMGg1TnRpY0VHdEV4TTR-fg"
        private var TOKEN =
            "T1==cGFydG5lcl9pZD00NzQ4MzY2MSZzaWc9NmUxNWY3MjM2NTQ4YzM0YzlkM2Y2NTUyN2M0MzE1M2RhNDgyMjAwOTpzZXNzaW9uX2lkPTFfTVg0ME56UTRNelkyTVg1LU1UWTBPVGcyT1RZME16VTNObjU1VFZGRFZtaHBaSFZ2TUdnMVRuUnBZMFZIZEVWNFRUUi1mZyZjcmVhdGVfdGltZT0xNjQ5ODY5Njk0Jm5vbmNlPTAuMTY2OTU1ODYxOTAzMzY5MDgmcm9sZT1wdWJsaXNoZXImZXhwaXJlX3RpbWU9MTY1MjQ2MTY5MyZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ=="
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