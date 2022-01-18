package kg.iaau.diploma.data

import androidx.room.Entity

@Entity
class User(
    val authorities: List<String> = listOf("USER"),
    var birthDate: String,
    var firstName: String,
    var lastName: String,
    var password: String,
    var patronymic: String,
    var username: String
)