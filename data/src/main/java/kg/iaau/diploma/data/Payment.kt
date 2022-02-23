package kg.iaau.diploma.data

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Payment(
    var id: Int? = null,
    var logo: String? = null,
    var name: String? = null,
    var nextSteps: String? = null,
    var paymentSteps: List<PaymentStep>? = null
)

data class PaymentStep(
    var id: Int? = null,
    var number: Int? = null,
    var text: String? = null
)