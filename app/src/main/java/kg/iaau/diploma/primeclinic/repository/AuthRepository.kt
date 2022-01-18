package kg.iaau.diploma.primeclinic.repository

import kg.iaau.diploma.data.AccessToken
import kg.iaau.diploma.data.Authorization
import kg.iaau.diploma.local_storage.StoragePreferences
import kg.iaau.diploma.network.interceptors.ApiAuth

class AuthRepository(
    private val prefs: StoragePreferences,
    private val apiAuth: ApiAuth
) {

    suspend fun auth(phone: String, password: String): AccessToken {
        val authorization = Authorization(phone, password)
        val response = apiAuth.auth(authorization)
        saveToken(response.accessToken)
        saveRefreshToken(response.refreshToken)
        saveChatToken(response.chatToken)
        return response
    }

    private fun saveToken(accessToken: String) {
        prefs.token = accessToken
    }

    private fun saveRefreshToken(refreshToken: String) {
        prefs.refreshToken = refreshToken
    }

    private fun saveChatToken(chatToken: String) {
        prefs.chatToken = chatToken
    }

}