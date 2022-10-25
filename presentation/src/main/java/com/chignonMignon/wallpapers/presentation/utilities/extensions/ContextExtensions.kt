package com.chignonMignon.wallpapers.presentation.utilities.extensions

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.chignonMignon.wallpapers.presentation.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

internal fun Context.color(@ColorRes colorResourceId: Int) = ContextCompat.getColor(this, colorResourceId)

internal fun Context.dimension(@DimenRes dimensionResourceId: Int) = resources.getDimensionPixelSize(dimensionResourceId)

internal fun Context.getWallpapersFolder() = File(filesDir, "wallpapers").also { it.mkdirs() }

internal fun Context.getWallpaperFile(id: String) = File("${getWallpapersFolder().path}/${id}.png")

internal fun Context.getUriForFile(file: File) =
    FileProvider.getUriForFile(applicationContext, applicationContext.packageName + ".fileProvider", file)

internal suspend fun Context.downloadImage(url: String) = ((ImageLoader(this).execute(
    ImageRequest.Builder(this).data(url).allowHardware(false).build()
) as? SuccessResult)?.drawable as? BitmapDrawable)?.bitmap

// TODO: Needs to be improved
internal fun Context.setWallpaper(uri: Uri) = Intent.createChooser(
    Intent(Intent.ACTION_SET_WALLPAPER, uri).addCategory(Intent.CATEGORY_DEFAULT).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION),
    getString(R.string.wallpaper_details_set_wallpaper)
).let { intent ->
    val results = packageManager?.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).orEmpty()
    if (results.isEmpty()) {
        startActivity(
            Intent.createChooser(
                Intent(Intent.ACTION_ATTACH_DATA)
                    .addCategory(Intent.CATEGORY_DEFAULT)
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    .setDataAndType(uri, "image/*")
                    .putExtra("mimeType", "image/png"),
                getString(R.string.wallpaper_details_set_wallpaper)
            )
        )
    } else {
        for (resolveInfo in results) {
            val packageName = resolveInfo.activityInfo.packageName
            grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(intent)
    }
}

internal fun Context.saveImage(file: File, bitmap: Bitmap): Uri? {
    val outputBytes = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputBytes)
    if (file.exists()) {
        file.delete()
    }
    return try {
        file.createNewFile()
        val fileOutputStream = FileOutputStream(file)
        fileOutputStream.write(outputBytes.toByteArray())
        fileOutputStream.close()
        getUriForFile(file)
    } catch (exception: IOException) {
        exception.printStackTrace()
        null
    }
}