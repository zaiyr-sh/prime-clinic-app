package kg.iaau.diploma.network.api

import kg.iaau.diploma.data.Faq
import retrofit2.http.GET

interface ApiFaq {

    @GET("faq/")
    suspend fun getFaq(): List<Faq>

}