package kg.iaau.diploma.network.api

import kg.iaau.diploma.data.MedCard
import kg.iaau.diploma.data.MedCardImage
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiMedCard {

    @PUT("client/card/{id}")
    suspend fun uploadMedCardById(
        @Path("id") id: Long?,
        @Body card: MedCard): MedCard

    @Multipart
    @PUT("client/{id}")
    suspend fun uploadMedCardImageById(
        @Path("id") id: Long?,
        @Part file: MultipartBody.Part): MedCard

    @GET("client/card/{id}")
    suspend fun getMedCardById(
        @Path("id") id: Long?
    ): MedCard

    @GET("client/{id}")
    suspend fun getMedCardImageById(
        @Path("id") id: Long?
    ): MedCardImage

}