package kg.iaau.diploma.core.utils

import android.net.Uri
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import kg.iaau.diploma.core.constants.UserType
import java.util.*

object FirebaseHelper {

    fun addCallListener(userId: String, listener: ((uid: String) -> Unit)? = null) {
        val ref = FirebaseFirestore.getInstance().collection("users").document(userId)
            .collection("call").document("calling")
        ref.addSnapshotListener { value, _ ->
            if (value != null && value.exists()) {
                value.getString("uid")?.let {
                    val uid = value.getString("uid")
                    if (uid != null && uid != "") listener?.invoke(uid)
                }
            }
        }
    }

    fun setUserOnline(userId: String) {
        val db = FirebaseFirestore.getInstance()
        val map = mutableMapOf<String, Any>()
        map["isOnline"] = true
        db.collection("users").document(userId).set(map, SetOptions.merge())
    }

    fun addAdminChatListener(
        userId: String,
        onSuccess: ((doc: DocumentSnapshot) -> Unit)? = null,
        onFail: ((ex: Exception) -> Unit)? = null
    ) {
        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("chatAdmin").document(userId)
        ref.get().addOnSuccessListener { doc ->
            onSuccess?.invoke(doc)
        }.addOnFailureListener {
            onFail?.invoke(it)
        }
    }

    inline fun <reified T> createChannels(userId: String): FirestoreRecyclerOptions<T> {
        val db = FirebaseFirestore.getInstance()
        val query = db.collection("PrimeDocChat").whereEqualTo("clientId", userId)
            .orderBy("lastMessageTime", Query.Direction.DESCENDING)
        return FirestoreRecyclerOptions.Builder<T>().setQuery(query, T::class.java).build()
    }

    fun setupUserType(
        docRef: DocumentReference?,
        userType: String,
        doctorChatListener: ((doc: DocumentSnapshot) -> Unit)? = null,
        adminChatListener: (() -> Unit)? = null
    ): String? {
        var userId: String? = ""
        docRef?.get()?.addOnSuccessListener {
            val adminId = it.getString("adminId")
            userId = adminId
            when (userType) {
                UserType.DOCTOR.name -> getDoctorData(adminId, doctorChatListener)
                UserType.ADMIN.name -> adminChatListener?.invoke()
            }
        }
        return userId
    }

    private fun getDoctorData(
        adminId: String?,
        doctorChatListener: ((doc: DocumentSnapshot) -> Unit)?
    ) {
        val db = FirebaseFirestore.getInstance()
        db.collection("doctors").document(adminId!!).get().addOnSuccessListener {
            doctorChatListener?.invoke(it)
        }
    }

    inline fun <reified T> setupChat(
        docRef: DocumentReference,
        crossinline onSuccess: ((options: FirestoreRecyclerOptions<T>) -> Unit)
    ) {
        val query = docRef.collection("messages").orderBy("time", Query.Direction.ASCENDING)
        val options: FirestoreRecyclerOptions<T> =
            FirestoreRecyclerOptions.Builder<T>().setQuery(query, T::class.java)
                .build()
        docRef.collection("messages").addSnapshotListener { _, _ ->
            onSuccess(options)
        }
    }

    fun uploadPhoto(imgUri: Uri, onSuccess: ((url: String) -> Unit)? = null, onDefault: (() -> Unit)? = null) {
        val ref = FirebaseStorage.getInstance().getReference("images/" + Date().time + ".jpg")
        ref.putFile(imgUri).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                val url = it.toString()
                if (url.isNotEmpty()) {
                    onSuccess?.invoke(url)
                }
                onDefault?.invoke()
            }
        }
    }

}