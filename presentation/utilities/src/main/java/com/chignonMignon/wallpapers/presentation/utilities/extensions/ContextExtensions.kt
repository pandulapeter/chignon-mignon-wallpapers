package com.chignonMignon.wallpapers.presentation.utilities.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import coil.ImageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.delay

@SuppressLint("ResourceAsColor")
@ColorInt
fun Context.colorResource(@AttrRes id: Int): Int {
    val resolvedAttr = TypedValue()
    theme.resolveAttribute(id, resolvedAttr, true)
    return color(resolvedAttr.run { if (resourceId != 0) resourceId else data })
}

fun Context.color(@ColorRes colorResourceId: Int) = ContextCompat.getColor(this, colorResourceId)

fun Context.dimension(@DimenRes dimensionResourceId: Int) = resources.getDimensionPixelSize(dimensionResourceId)

suspend fun Context.downloadImage(url: String, retryCount: Int = 3): Bitmap? {
    val result = ImageLoader(this).execute(
        ImageRequest.Builder(this).data(url).allowHardware(false).build()
    )
    if (retryCount > 0 && (result is ErrorResult)) {
        delay(200)
        return downloadImage(url, retryCount - 1)
    }
    return ((result as? SuccessResult)?.drawable as? BitmapDrawable)?.bitmap
}