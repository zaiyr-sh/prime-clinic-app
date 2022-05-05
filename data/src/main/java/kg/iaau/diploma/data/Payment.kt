package kg.iaau.diploma.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Payment(
    var id: Int? = null,
    var logo: String? = null,
    var name: String? = null,
    var nextSteps: String? = null,
    var paymentSteps: List<PaymentStep>? = null
) : Parcelable

@Parcelize
data class PaymentStep(
    var id: Int? = null,
    var number: Int? = null,
    var text: String? = null
) : Parcelable