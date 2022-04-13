package kg.iaau.diploma.primeclinic.ui.main.chat

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.constants.MIMETYPE_IMAGES
import kg.iaau.diploma.core.constants.MessageType
import kg.iaau.diploma.core.constants.UserType
import kg.iaau.diploma.core.ui.CoreFragment
import kg.iaau.diploma.core.ui.LoadingScreen
import kg.iaau.diploma.core.utils.*
import kg.iaau.diploma.data.Message
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentChatBinding
import kg.iaau.diploma.primeclinic.ui.main.chat.adapter.MessageAdapter
import kg.iaau.diploma.primeclinic.ui.main.chat.adapter.MessageListener
import kg.iaau.diploma.primeclinic.ui.main.chat.calling.CallingActivity

@AndroidEntryPoint
class ChatFragment : CoreFragment<FragmentChatBinding, ChatVM>(ChatVM::class.java), MessageListener {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentChatBinding =
        FragmentChatBinding::inflate

    private lateinit var adapter: MessageAdapter

    private val args: ChatFragmentArgs by navArgs()
    private val ref by lazy { args.path }
    private val userType by lazy { args.type }

    private var docRef: DocumentReference? = null
    private lateinit var db: FirebaseFirestore

    private var canWrite: Boolean = true
    private var messageType: String = MessageType.TEXT.type
    private var userId: String? = ""
    private var image: String = ""
    private var imgUri: Uri? = null

    private var pickImage: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            vb.run {
                ivAttach.setImageURI(uri)
                ivAttach.show()
                imgUri = uri
                messageType = MessageType.IMAGE.type
            }
        }
    }

    private var requestPickImagePermission: ActivityResultLauncher<String> = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) pickImage.launch(MIMETYPE_IMAGES)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().toast(getString(R.string.chat_started))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.chat_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.video_call -> {
                if (userId != null && canWrite && userType != UserType.ADMIN.name)
                    makeVideoCall()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun setupFragmentView() {
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(vb.toolbar)
        setupChatWithFirebase()
        vb.run {
            toolbar.setNavigationOnClickListener {
                parentFragmentManager.popBackStack()
            }
            rlAttachImage.setOnClickListener {
                requestPickImagePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            ivSentMessage.setOnClickListener {
                if (etMessageTyping.text.toString().isNotEmpty()) {
                    if (messageType == "text")
                        sendMessage()
                    else
                        uploadPhotoToCloud()
                }
            }
            etMessageTyping.requestFocus()
            setupChat()
        }
    }

    private fun setupChatWithFirebase() {
        initFirebaseAuth()
        db = FirebaseFirestore.getInstance()
        docRef = ref.let { db.document(it) }
        FirebaseHelper.setupUserType(
            docRef,
            listener = { id ->
                userId = id
                when (userType) {
                    UserType.DOCTOR.name -> FirebaseHelper.setupDoctorData(
                        id,
                        doctorChatListener = { snapshot -> setupDoctorChat(snapshot) }
                    )
                    UserType.ADMIN.name -> setHasOptionsMenu(false)
                }
            }
        )
    }

    private fun setupDoctorChat(doc: DocumentSnapshot) {
        vb.run {
            val image = doc.getString("image")
            val name = doc.getString("name")
            val fatherName = doc.getString("fatherName")
            toolbarLogo.loadWithFresco(image, onFail = {
                toolbarLogo.setActualImageResource(R.drawable.shape_filled_dot)
            })
            toolbar.title = "$name $fatherName"
        }
    }

    private fun sendMessage() {
        vb.run {
            ivAttach.gone()
            val msg = etMessageTyping.text.toString()
            etMessageTyping.setText("")
            val user = mAuth.currentUser!!
            val message = Message(user.uid, "", msg, Timestamp.now(), messageType, image)
            messageType = MessageType.TEXT.type
            docRef?.collection("messages")?.document()?.set(message)
                ?.addOnCompleteListener {
                    when(userType) {
                        UserType.DOCTOR.name -> sendMessageToDoctor(msg)
                        UserType.ADMIN.name -> sendMessageToAdmin(msg)
                    }
                }
        }
    }

    private fun sendMessageToAdmin(msg: String) {
        val map = mutableMapOf<String, Any>()
        map["adminId"] = "a"
        map["adminPhone"] = ""
        map["clientId"] = vm.userId.toString()
        map["lastMessage"] = msg
        map["lastMessageSenderId"] = vm.userId.toString()
        map["lastMessageTime"] = Timestamp.now()
        map["name"] = vm.phone ?: ""
        map["surname"] = "USER"
        docRef?.set(map, SetOptions.merge())
    }

    private fun sendMessageToDoctor(msg: String) {
        val map = mutableMapOf<String, Any>()
        map["lastMessage"] = msg
        map["lastMessageSenderId"] = vm.userId.toString()
        map["lastMessageTime"] = Timestamp.now()
        docRef?.set(map, SetOptions.merge())
    }

    private fun setupChat() {
        vb.run {
            rvChats.setHasFixedSize(true)
            val observer = object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                    super.onItemRangeChanged(positionStart, itemCount)
                    rvChats.scrollToPosition(positionStart)
                }
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    super.onItemRangeInserted(positionStart, itemCount)
                    rvChats.scrollToPosition(positionStart)
                }
            }
            FirebaseHelper.setupChat<Message>(docRef!!) { options ->
                adapter = MessageAdapter(options, this@ChatFragment)
                rvChats.adapter = adapter
                adapter.startListening()
                adapter.registerAdapterDataObserver(observer)
            }
        }
    }

    private fun uploadPhotoToCloud() {
        LoadingScreen.showLoading(requireActivity())
        FirebaseHelper.uploadPhoto(imgUri!!,
            onSuccess = { url ->
                image = url
                sendMessage()
            },
            onDefault = {
                LoadingScreen.hideLoading()
            }
        )
    }

    private fun makeVideoCall() {
        CallingActivity.startActivity(requireActivity(), userId!!)
    }

    override fun onImageClick(image: String?) {
        findNavController().navigate(
            R.id.nav_image_full,
            Bundle().apply {
                putString("image", image)
            }
        )
    }

}