package kg.iaau.diploma.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Faq(
    @PrimaryKey
    var id: Int? = null,
    var question: String? = null,
    var order: Int? = null,
    var answer: String? = null
)