package kg.iaau.diploma.local_storage

import android.content.Context

class StoragePreferences(context: Context) : BasePrefs(context) {

    override val prefFileName: String
        get() = "kg.iaau.diploma.local_storage"

    var token: String by PrefDelegate(sharedPreference, Keys.ACCESS_TOKEN, "")
    var refreshToken: String by PrefDelegate(sharedPreference, Keys.REFRESH_TOKEN, "")
    var chatToken: String by PrefDelegate(sharedPreference, Keys.CHAT_TOKEN, "")

    object Keys {
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val REFRESH_TOKEN = "REFRESH_TOKEN"
        const val CHAT_TOKEN = "CHAT_TOKEN"
    }
}