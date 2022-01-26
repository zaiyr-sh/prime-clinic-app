package kg.iaau.diploma.network.api

import kg.iaau.diploma.data.AccessToken
import kg.iaau.diploma.data.Authorization
import kg.iaau.diploma.data.User
import retrofit2.http.*

interface ApiAuth {

    @POST("auth")
    suspend fun auth(@Body authorization: Authorization): AccessToken

    @POST("register")
    suspend fun register(@Body user: User)

    @POST("verify/{code}")
    suspend fun verify(@Path("code") code: String)

}