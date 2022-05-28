package kg.iaau.diploma.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class MedCard(
    @PrimaryKey
    var id: Int? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var patronymic: String? = null,
    var birthDate: String? = null,
    var medCardPhoneNumber: String? = null
) {

    fun isNullOrEmpty(): Boolean {
        return id == null
                && firstName.isNullOrEmpty()
                && lastName.isNullOrEmpty()
                && patronymic.isNullOrEmpty()
                && birthDate.isNullOrEmpty()
                && medCardPhoneNumber.isNullOrEmpty()
    }

}

class MedCardImage(
    var id: Int? = null,
    var image: String? = null,
    var medCardPhoneNumber: String? = null,
)