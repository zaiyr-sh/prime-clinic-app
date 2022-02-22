package kg.iaau.diploma.data

data class Interval(
    var id: Int? = 0,
    var start: String? = null,
    var end: String? = null,
    var reservation: List<Slot>? = null
)

data class Slot(
    var id: Int? = 0,
    var start: String? = null,
    var end: String? = null,
    var phoneNumber: String? = null,
    var comment: String? = null,
    var paid: Boolean? = false
)