package kg.iaau.diploma.network.api

import kg.iaau.diploma.data.About
import retrofit2.http.GET

interface ApiAbout {

    @GET("api/v1/about/")
    suspend fun getInfoAboutUs(): List<About>

}