package kg.iaau.diploma.data

import androidx.room.Entity

@Entity
data class AccessToken(
    var id: Int,
    var accessToken: String,
    var chatToken: String,
    var tokenExpirationTime: String,
    var refreshToken: String,
    var refreshExpirationTime: String
)