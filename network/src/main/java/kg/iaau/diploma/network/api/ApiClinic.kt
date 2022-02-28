package kg.iaau.diploma.network.api

import kg.iaau.diploma.data.*
import retrofit2.http.*

interface ApiClinic {

    @GET("category/info")
    suspend fun getSpecialistCategories(@Query("page") page: Int): Pageable

    @GET("category/info/details/{id}")
    suspend fun getSpecialistsCategoryDetailInfo(@Path("id") id: Long): SpecialistCategory

    @GET("doctor/profile/{id}")
    suspend fun getDoctorProfileById(@Path("id") id: Long): Doctor

    @GET("worktime/relevant/{id}")
    suspend fun getScheduleByDoctorId(@Path("id") id: Long?): List<Interval>

    @PUT("worktime/reserve")
    suspend fun reserveVisit(@Body reservation: Reservation): ReservationInfo

    @GET("payment/")
    suspend fun getPaymentMethods(): List<Payment>
}