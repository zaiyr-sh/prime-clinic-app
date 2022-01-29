package kg.iaau.diploma.primeclinic.repository

import kg.iaau.diploma.data.MedCard
import kg.iaau.diploma.local_storage.db.MedCardDao
import kg.iaau.diploma.local_storage.prefs.StoragePreferences
import kg.iaau.diploma.network.api.ApiMedCard

class MedCardRepository(
    private val prefs: StoragePreferences,
    private val apiMedCard: ApiMedCard,
    private val medCardDao: MedCardDao
) {

    private val userId: Long? = prefs.userId

    suspend fun uploadMedCard(medCard: MedCard) = apiMedCard.uploadMedCardById(userId, medCard)

    suspend fun getMedCard() = apiMedCard.getMedCardById(userId)

    suspend fun saveMedCardInDb(medCard: MedCard) = medCardDao.saveMedCardInDb(medCard)

    suspend fun getMedCardFromDb() = medCardDao.getMedCardFromDb()

}