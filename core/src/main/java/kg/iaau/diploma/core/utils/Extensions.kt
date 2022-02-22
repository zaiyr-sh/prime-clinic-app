package kg.iaau.diploma.core.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kg.iaau.diploma.core.R
import kg.iaau.diploma.core.constants.CHANNEL_ID
import kg.iaau.diploma.core.constants.NOTIFICATION_ID

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

fun View.setAnimateAlpha(value: Float) {
    animate().alpha(value)
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

fun Context.pushNotification(title: String?, message: String?) {
    val builder = NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_heart)
        .setContentTitle(title)
        .setContentText(message)
        .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setCategory(NotificationCompat.CATEGORY_NAVIGATION)
        .setAutoCancel(true)
    with(NotificationManagerCompat.from(this)) {
        notify(NOTIFICATION_ID, builder.build())
    }
}

fun String.convertBase64ToBitmap(): Bitmap {
    val imageAsBytes = Base64.decode(toByteArray(), Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
}

fun String.convertBase64ToDrawable(context: Context): Drawable {
    val bitmap = this.convertBase64ToBitmap()
    return BitmapDrawable(context.resources, bitmap)
}