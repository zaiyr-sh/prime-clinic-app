package kg.iaau.diploma.network.api

import kg.iaau.diploma.data.MedCard
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiMedCard {

    @PUT("client/card/{id}")
    suspend fun uploadMedCardById(
        @Path("id") id: Long?,
        @Body card: MedCard): MedCard

    @GET("client/card/{id}")
    suspend fun getMedCardById(
        @Path("id") id: Long?
    ): MedCard

}