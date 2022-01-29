package kg.iaau.diploma.core.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast

inline fun <reified T : Activity> Context.startActivity(noinline extra: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    intent.extra()
    startActivity(intent)
}

fun String.convertPhoneNumberTo(countryCode: String): String = "+$countryCode$this"

fun Context.toast(message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, duration).show()
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.setEnable(enabled: Boolean) {
    isEnabled = enabled
    alpha = if (enabled) 1f else 0.5f
}

val String.isNotField: Boolean
    get() = isEmpty() || length == 1

val String.isPhoneNotField: Boolean
    get() = isEmpty() || length != 13

val String.isDateNotField: Boolean
    get() = isEmpty() || length != 10

// Convert birth date to UTC format date
fun String.convertToUTC(): String {
    val day = substring(0,2)
    val month = substring(3,5)
    val year = substring(6)
    return year+"-"+month+"-"+day+"T11:00:00.320Z"
}