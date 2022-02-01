package kg.iaau.diploma.data

import androidx.room.Entity

@Entity
data class Pageable(
    var content: List<SpecialistCategory>? = null,
    var empty: Boolean? = false,
    var first: Boolean? = false,
    var last: Boolean? = false,
    var number: Int? = null,
    var numberOfElements: Int? = null,
    var size: Int? = null,
    var totalElements: Int? = null,
    var totalPages: Int? = null
)