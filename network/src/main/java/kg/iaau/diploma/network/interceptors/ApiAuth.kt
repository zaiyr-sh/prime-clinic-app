package kg.iaau.diploma.network.interceptors

import kg.iaau.diploma.data.AccessToken
import kg.iaau.diploma.data.Authorization
import kg.iaau.diploma.data.User
import retrofit2.http.*

interface ApiAuth {

    @POST("api/v1/auth")
    suspend fun auth(@Body authorization: Authorization): AccessToken

    @POST("api/v1/register")
    suspend fun register(@Body user: User): Void

    @POST("api/v1/verify/{code}")
    suspend fun verify(@Path("code") code: String): Void

}