package kg.iaau.diploma.network.api

import kg.iaau.diploma.data.About
import retrofit2.http.GET

interface ApiAbout {

    @GET("about/")
    suspend fun getInfoAboutUs(): List<About>

}