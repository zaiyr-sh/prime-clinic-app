package kg.iaau.diploma.primeclinic.repository

import kg.iaau.diploma.data.Faq
import kg.iaau.diploma.local_storage.db.FaqDao
import kg.iaau.diploma.network.api.ApiFaq

class FaqRepository(
    private val apiFaq: ApiFaq,
    private val faqDao: FaqDao
) {

    suspend fun getFaq() = apiFaq.getFaq()

    suspend fun insertFaqs(faqs: List<Faq>) = faqDao.insertFaqs(faqs)

    suspend fun getFaqFromDb() = faqDao.getFaqFromDb()

}