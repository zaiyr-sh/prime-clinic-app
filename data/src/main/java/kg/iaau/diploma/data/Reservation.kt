package kg.iaau.diploma.data

data class Reservation(
    var clientId: Long? = null,
    var worktimeId: Int? = null,
    var start: String? = null,
    var end: String? = null,
    var comment: String? = null,
    var phoneNumber: String? = null
)
