package kg.iaau.diploma.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class MedCard(
    @PrimaryKey
    var id: Int? = null,
    var image: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var patronymic: String? = null,
    var birthDate: String? = null,
    var medCardPhoneNumber: String? = null,
    var agreed: Boolean? = false
)