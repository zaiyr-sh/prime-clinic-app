package kg.iaau.diploma.core.utils

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

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

}