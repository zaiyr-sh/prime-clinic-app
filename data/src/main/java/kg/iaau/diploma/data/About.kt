package kg.iaau.diploma.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class About(
    @PrimaryKey
    var id: Int? = null,
    var header: String? = null,
    var paragraph: String? = null,
    var order: Int? = null
)