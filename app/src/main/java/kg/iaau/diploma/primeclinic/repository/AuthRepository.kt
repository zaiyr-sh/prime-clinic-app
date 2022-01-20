package kg.iaau.diploma.primeclinic.repository

import kg.iaau.diploma.data.AccessToken
import kg.iaau.diploma.data.Authorization
import kg.iaau.diploma.data.User
import kg.iaau.diploma.local_storage.StoragePreferences
import kg.iaau.diploma.network.interceptors.ApiAuth

class AuthRepository(
    private val prefs: StoragePreferences,
    private val apiAuth: ApiAuth
) {
    var token: String? = prefs.token
    var phone: String? = prefs.phone
    var deviceId: String? = prefs.deviceId

    suspend fun auth(phone: String, password: String): AccessToken {
        val authorization = Authorization(phone, password)
        val response = apiAuth.auth(authorization)
        saveToken(response.accessToken)
        saveRefreshToken(response.refreshToken)
        return response
    }

    suspend fun register(phone: String, password: String) {
        val authorization = User(username = phone, password = password)
        apiAuth.register(authorization)
    }

    suspend fun verify(code: String) {
        apiAuth.verify(code)
    }

    private fun saveToken(accessToken: String?) {
        prefs.token = accessToken
    }

    private fun saveRefreshToken(refreshToken: String?) {
        prefs.refreshToken = refreshToken
    }

    fun getPin() = prefs.pin

    fun savePin(pin: String) {
        prefs.pin = pin
    }

    fun restorePinWithTokens() {
        prefs.token = ""
        prefs.refreshToken = ""
        prefs.pin = ""
    }

}