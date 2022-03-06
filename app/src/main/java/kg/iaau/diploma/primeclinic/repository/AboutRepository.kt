package kg.iaau.diploma.primeclinic.repository

import kg.iaau.diploma.data.About
import kg.iaau.diploma.local_storage.db.AboutDao
import kg.iaau.diploma.local_storage.prefs.StoragePreferences
import kg.iaau.diploma.network.api.ApiAbout

class AboutRepository(
    private val prefs: StoragePreferences,
    private val apiAbout: ApiAbout,
    private val aboutDao: AboutDao
) {

    suspend fun getInfoAboutUs() = apiAbout.getInfoAboutUs()

    suspend fun insertAboutUsInfo(aboutInfo: List<About>) = aboutDao.insertAboutUsInfo(aboutInfo)

    suspend fun getInfoAboutUsFromDb() = aboutDao.getInfoAboutUsFromDb()

    fun restorePinWithTokens() {
        prefs.token = ""
        prefs.refreshToken = ""
        prefs.pin = ""
    }

}