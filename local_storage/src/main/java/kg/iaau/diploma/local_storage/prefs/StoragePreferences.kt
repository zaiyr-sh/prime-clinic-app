package kg.iaau.diploma.local_storage.prefs

import android.content.Context

class StoragePreferences(context: Context) : BasePrefs(context) {

    override val prefFileName: String
        get() = "kg.iaau.diploma.local_storage.prefs"

    var id: Long? by PrefDelegate(sharedPreference, Keys.ID, 0)
    var userId: Long? by PrefDelegate(sharedPreference, Keys.USER_ID, 0)
    var token: String? by PrefDelegate(sharedPreference, Keys.ACCESS_TOKEN, "")
    var refreshToken: String? by PrefDelegate(sharedPreference, Keys.REFRESH_TOKEN, "")
    var pin: String? by PrefDelegate(sharedPreference, Keys.PIN, "")
    var phone: String? by PrefDelegate(sharedPreference, Keys.PHONE, "")
    var deviceId: String? by PrefDelegate(sharedPreference, Keys.DEVICE_ID, "")

    object Keys {
        const val ID = "ID"
        const val USER_ID = "USER_ID"
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val REFRESH_TOKEN = "REFRESH_TOKEN"
        const val PIN = "PIN"
        const val PHONE = "PHONE"
        const val DEVICE_ID = "DEVICE_ID"
    }
}