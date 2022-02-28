package kg.iaau.diploma.data

data class Reservation(
    var clientId: Long? = null,
    var worktimeId: Int? = null,
    var start: String? = null,
    var end: String? = null,
    var comment: String? = null,
    var phoneNumber: String? = null
)

data class ReservationInfo(
    var id: Int? = null,
    var start: String? = null,
    var end: String? = null,
    var reservation: ArrayList<ReservationStatus>? = null
)

data class ReservationStatus(
    var id: Int? = null,
    var start: String? = null,
    var end: String? = null,
    var phoneNumber: String? = null,
    var comment: String? = null,
    var bill: String? = null,
    var paid: Boolean? = null,
    var checkUrl: String? = null

)
