package kg.iaau.diploma.network.api

import kg.iaau.diploma.data.Doctor
import kg.iaau.diploma.data.Pageable
import kg.iaau.diploma.data.SpecialistCategory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiClinic {

    @GET("category/info")
    suspend fun getSpecialistCategories(@Query("page") page: Int): Pageable

    @GET("category/info/details/{id}")
    suspend fun getSpecialistsCategoryDetailInfo(@Path("id") id: Long): SpecialistCategory

    @GET("doctor/profile/{id}")
    suspend fun getDoctorProfileById(@Path("id") id: Long?): Doctor
}