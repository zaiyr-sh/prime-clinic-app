package kg.iaau.diploma.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SpecialistCategory(
    @PrimaryKey
    var id: Long? = null,
    var description: String? = null,
    var image: String? = null,
    var name: String? = null,
    var doctors: List<Doctor>? = null
)

@Entity
data class Doctor(
    var id: Long? = 0,
    var bio: String? = null,
    var image: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var patronymic: String? = null,
    var information: List<Information>? = null,
    var position: String? = null
)

@Entity
data class Information(
    var end: String? = null,
    var name: String? = null,
    var organizationName: String? = null,
    var start: String? = null
)
