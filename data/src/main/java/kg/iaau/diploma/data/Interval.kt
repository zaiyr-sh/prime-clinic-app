package kg.iaau.diploma.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Interval(
    var id: Int? = null,
    var start: String? = null,
    var end: String? = null,
    var reservation: List<Slot>? = null
) : Parcelable

@Parcelize
data class Slot(
    var id: Int? = null,
    var start: String? = null,
    var end: String? = null,
    var phoneNumber: String? = null,
    var comment: String? = null,
    var paid: Boolean? = false
) : Parcelable