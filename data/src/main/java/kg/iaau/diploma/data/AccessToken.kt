package kg.iaau.diploma.data

import androidx.room.Entity

@Entity
data class AccessToken(
    var id: Int? = null,
    var userId: Int? = null,
    var accessToken: String? = null,
    var tokenExpirationTime: String? = null,
    var refreshToken: String? = null,
    var refreshExpirationTime: String? = null
)