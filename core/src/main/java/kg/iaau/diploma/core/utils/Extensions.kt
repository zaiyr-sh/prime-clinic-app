package kg.iaau.diploma.core.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Animatable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Base64
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.image.ImageInfo
import com.google.android.material.snackbar.Snackbar
import kg.iaau.diploma.core.R
import kg.iaau.diploma.core.constants.DD_MM_YYYY
import kg.iaau.diploma.core.constants.DD_MM_YYYY_HH_MM
import kg.iaau.diploma.core.constants.YYYY_MM_DD
import kg.iaau.diploma.core.constants.YYYY_MM_DD_T_HH_MM_SSS
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
    val format = SimpleDateFormat(YYYY_MM_DD_T_HH_MM_SSS, Locale.getDefault()).parse(this)
    return SimpleDateFormat(DD_MM_YYYY, Locale.getDefault()).format(format)
}

fun Date.formatForDate(format: String = DD_MM_YYYY_HH_MM): String {
    val sdf = SimpleDateFormat(format, Locale.ROOT)
    return sdf.format(this)
}

fun Long.formatForDate(format: String = YYYY_MM_DD): String {
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

fun SimpleDraweeView.loadWithFresco(
    uri: String?,
    onSuccess: ((imageInfo: ImageInfo?) -> Unit)? = null,
    onFail: ((throwable: Throwable) -> Unit)? = null
) {
    val controller: DraweeController = Fresco.newDraweeControllerBuilder()
        .setUri(uri)
        .setTapToRetryEnabled(true)
        .setOldController(controller)
        .setControllerListener(frescoListener(onSuccess, onFail))
        .build()

    setController(controller)
}

fun SimpleDraweeView.loadWithFresco(
    uri: Uri?,
    onSuccess: ((imageInfo: ImageInfo?) -> Unit)? = null,
    onFail: ((throwable: Throwable) -> Unit)? = null
) {
    val controller: DraweeController = Fresco.newDraweeControllerBuilder()
        .setUri(uri)
        .setTapToRetryEnabled(true)
        .setOldController(controller)
        .setControllerListener(frescoListener(onSuccess, onFail))
        .build()

    setController(controller)
}

fun frescoListener(
    onSuccess: ((imageInfo: ImageInfo?) -> Unit)?,
    onFail: ((throwable: Throwable) -> Unit)?
): BaseControllerListener<ImageInfo?> {
    return object : BaseControllerListener<ImageInfo?>() {
        override fun onFinalImageSet(
            id: String?,
            @Nullable imageInfo: ImageInfo?,
            @Nullable animatable: Animatable?
        ) {
            onSuccess?.invoke(imageInfo)
        }

        override fun onFailure(id: String, throwable: Throwable) {
            onFail?.invoke(throwable)
        }
    }
}

fun Context.getImageFileUri(appId: String, fileName: String): Uri {
    val file = File.createTempFile(fileName, ".png").apply {
        createNewFile()
        deleteOnExit()
    }
    return FileProvider.getUriForFile(this, "${appId}.provider", file)
}

fun RecyclerView.scrollToLastItem() {
    addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
        if (bottom < oldBottom) {
            postDelayed({
                smoothScrollToPosition(bottom)
            }, 100)
        }
    }
}