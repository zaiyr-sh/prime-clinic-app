package kg.iaau.diploma.core.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Animatable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.text.Spanned
import android.util.Base64
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
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

fun View.setVisible(isVisible: Boolean) {
    visibility = when (isVisible) {
        true -> View.VISIBLE
        else -> View.GONE
    }
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
    val day = substring(8,10)
    val month = substring(5,7)
    val year = substring(0, 4)
    return "$year-$month-$day"
}

fun String.convertBase64ToBitmap(): Bitmap? {
    val imageAsBytes = Base64.decode(toByteArray(), Base64.DEFAULT)
    if (imageAsBytes.isEmpty()) return null
    return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
}

fun String.convertBase64ToDrawable(context: Context, @DrawableRes defaultResId: Int): Drawable? {
    return when(isNullOrEmpty()) {
        true -> context.setDrawable(defaultResId)
        else -> {
            val bitmap = this.convertBase64ToBitmap()
            BitmapDrawable(context.resources, bitmap)
        }
    }
}

fun Bitmap.convertBitmapToBase64(): String? {
    val outputStream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    val byteArray: ByteArray = outputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun Resources.convertDrawableToBitmap(id: Int): Bitmap? {
    return BitmapFactory.decodeResource(this, id)
}

fun Uri.convertUriToBitmap(cr: ContentResolver): Bitmap? {
    return MediaStore.Images.Media.getBitmap(cr, this)
}

fun ImageView.loadBase64Image(context: Context, image: String?, @DrawableRes defaultResId: Int) {
    if (image.isNullOrEmpty() || image.isNullOrBlank())
        setImageDrawable(context.setDrawable(defaultResId))
    else
        setImageDrawable(image.convertBase64ToDrawable(context, defaultResId))
}

fun Context.setDrawable(@DrawableRes id: Int): Drawable? {
    return ContextCompat.getDrawable(this, id)
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

private fun frescoListener(
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

fun Context.getUriForFile(onPath: ((absolutePath: String?) -> Unit)? = null): Uri? {
    return FileProvider.getUriForFile(this, "kg.iaau.diploma.primeclinic.provider", createImageFile().also {
        onPath?.invoke(it.absolutePath)
    })
}

fun Context.createImageFile(): File {
    val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile("temp_image", ".jpg", storageDir)
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

fun File.createFormData(key: String): MultipartBody.Part {
    return MultipartBody.Part.createFormData(
        key,
        this.absolutePath,
        asRequestBody("multipart/form-data".toMediaTypeOrNull())
    )
}

@SuppressLint("CheckResult")
fun Context.loadWithGlide(image: String?, onSuccess: ((resource: Drawable?) -> Unit)? = null, onFail: (() -> Unit)? = null) {
    Glide.with(this).load(image)
        .listener(object : RequestListener<Drawable> {
            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                onSuccess?.invoke(resource)
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

}

fun Date.remainFromInDays(date2: Date): Long {
    val diff: Long = time - date2.time
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    return days
}

fun Context.setColor(@ColorRes resId: Int): Int {
    return ContextCompat.getColor(this, resId)
}

fun String?.isFullyEmpty() = isNullOrEmpty() || isNullOrBlank()

fun String?.toHtml(): Spanned? {
    if (this.isNullOrEmpty()) return null
    return HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_COMPACT)
}

fun View.setOnSingleClickListener(onClick: (View) -> Unit) {
    val safeClickListener = SingleClickListener {
        onClick(it)
    }
    setOnClickListener(safeClickListener)
}