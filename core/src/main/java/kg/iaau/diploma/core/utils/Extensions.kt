package kg.iaau.diploma.core.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Base64
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import kg.iaau.diploma.core.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

inline fun <reified T : Activity> Context.startActivity(noinline extra: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    intent.extra()
    startActivity(intent)
}

fun String.convertPhoneNumberWithCode(countryCode: String): String = "+$countryCode$this"

fun String.convertToEmail(): String = "$this@gmail.com"

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun View.showSnackBar(context: Context, message: String) {
    val snackBar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    snackBar.setTextColor(ContextCompat.getColor(context, R.color.white))
    snackBar.setBackgroundTint(ContextCompat.getColor(context, R.color.teal))
    snackBar.show()

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

fun Context.showDialog(
    @StringRes title: Int,
    positiveCallback: (() -> Unit)? = null,
    negativeCallback: (() -> Unit)? = null
) {
    AlertDialog.Builder(this, R.style.AlertDialogTheme)
        .setTitle(getString(title))
        .setCancelable(false)
        .setPositiveButton(R.string.ok) { dialog, _ ->
            positiveCallback?.invoke()
            dialog.cancel()
        }
        .setNegativeButton(R.string.cancel) { dialog, _ ->
            negativeCallback?.invoke()
            dialog.cancel()
        }
        .show()
}

val String.isNotField: Boolean
    get() = isEmpty() || length == 1

val String.isPhoneNotFieldCorrectly: Boolean
    get() = isEmpty() || length != 13 || !startsWith("+996")

// Convert birth date to UTC format date
fun String.convertToUTC(): String {
    val day = substring(0,2)
    val month = substring(3,5)
    val year = substring(6)
    return year+"-"+month+"-"+day+"T11:00:00.320Z"
}

fun String.convertBase64ToBitmap(): Bitmap {
    val imageAsBytes = Base64.decode(toByteArray(), Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
}

fun String.convertBase64ToDrawable(context: Context): Drawable {
    val bitmap = this.convertBase64ToBitmap()
    return BitmapDrawable(context.resources, bitmap)
}

fun String.formatForCurrentDate(): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SSS", Locale.getDefault()).parse(this)
    return SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(format)
}

fun Date.formatForDate(format: String = "dd.MM.yyyy HH:mm"): String {
    val sdf = SimpleDateFormat(format, Locale.ROOT)
    return sdf.format(this)
}

fun Long.formatForDate(format: String = "yyyy-MM-dd"): String {
    val sdf = SimpleDateFormat(format, Locale.ROOT)
    return sdf.format(this)
}

fun View.isDrawableEqual(context: Context, drawable: Int): Boolean {
    return Objects.equals(background.constantState, context.resources.getDrawable(drawable).constantState)
}


fun Fragment.hideKeyboard() {
    view?.let {
        activity?.hideKeyboard(it)
    }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.loadWithGlide(imageView: ImageView, image: String?, onSuccess: (() -> Unit)? = null, onFail: (() -> Unit)? = null) {
    Glide.with(this).load(image)
        .listener(object : RequestListener<Drawable> {
            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                onSuccess?.invoke()
                return false
            }
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                onFail?.invoke()
                return false
            }

        })
        .error(R.drawable.ic_error)
        .into(imageView)
}

fun Context.getImageFileUri(appId: String, fileName: String): Uri {
    val file = File.createTempFile(fileName, ".png").apply {
        createNewFile()
        deleteOnExit()
    }
    return FileProvider.getUriForFile(this, "${appId}.provider", file)
}