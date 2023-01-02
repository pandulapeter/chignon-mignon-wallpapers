package com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation

import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import android.widget.Toast
import com.chignonMignon.wallpapers.presentation.shared.R
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal suspend fun Context.setWallpaperBitmap(bitmap: Bitmap, visibleRect: Rect) = suspendCoroutine { continuation ->
    try {
        WallpaperManager.getInstance(this@setWallpaperBitmap).setBitmap(bitmap, visibleRect, true)
        continuation.resume(true)
    } catch (_: IOException) {
        continuation.resume(false)
    }
}

/**
 * The functions below are not currently used, but they are not deleted to help future experiments.
 */
internal fun Context.setWallpaper(uri: Uri) = setWallpaperOptionC(uri)

/**
 * Opens a chooser showing all apps that respond to the ACTION_SET_WALLPAPER intent.
 * Results:
 *  - On Samsung it works perfectly.
 *  - On Motorola it shows and empty bottom sheet.
 */
private fun Context.setWallpaperOptionA(uri: Uri) = startActivity(
    Intent.createChooser(
        Intent(Intent.ACTION_SET_WALLPAPER)
            .addCategory(Intent.CATEGORY_DEFAULT)
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            .setDataAndType(uri, "image/*")
            .putExtra("mimeType", "image/png"),
        getString(R.string.wallpaper_details_set_wallpaper)
    )
)

/**
 * Opens a chooser showing all apps that respond to the ACTION_ATTACH_DATA intent.
 * Results:
 *  - Brings up irrelevant apps such as Contacts and WhatsApp.
 *  - On many phones it only works if Google Photos is installed.
 */
private fun Context.setWallpaperOptionB(uri: Uri) = startActivity(
    Intent.createChooser(
        Intent(Intent.ACTION_ATTACH_DATA)
            .addCategory(Intent.CATEGORY_DEFAULT)
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            .setDataAndType(uri, "image/*")
            .putExtra("mimeType", "image/png"),
        getString(R.string.wallpaper_details_set_wallpaper)
    )
)

/**
 * Sets the wallpaper automatically without opening any other apps.
 * Results:
 *  - Bad but (at least) consistent UX.
 */
private fun Context.setWallpaperOptionC(uri: Uri) = try {
    WallpaperManager.getInstance(this).setStream(contentResolver.openInputStream(uri))
    Toast.makeText(this, R.string.wallpaper_details_wallpaper_applied_successfully, Toast.LENGTH_SHORT).show()
} catch (_: IOException) {
    Toast.makeText(this, R.string.wallpaper_details_cannot_set_wallpaper_apply, Toast.LENGTH_SHORT).show()
}