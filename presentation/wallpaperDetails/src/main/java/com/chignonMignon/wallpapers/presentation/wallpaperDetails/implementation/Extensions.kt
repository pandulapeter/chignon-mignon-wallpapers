package com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.wallpaperList.WallpaperType
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal suspend fun Context.setWallpaperBitmap(bitmap: Bitmap, visibleRect: Rect, wallpaperType: WallpaperType) = suspendCoroutine { continuation ->
    try {
        WallpaperManager.getInstance(this@setWallpaperBitmap).setBitmap(
            bitmap,
            visibleRect,
            true,
            wallpaperType.toFlags()
        )
        continuation.resume(true)
    } catch (_: IOException) {
        continuation.resume(false)
    }
}

private fun WallpaperType.toFlags() = when (this) {
    WallpaperType.HOME_SCREEN -> WallpaperManager.FLAG_SYSTEM
    WallpaperType.LOCK_SCREEN -> WallpaperManager.FLAG_LOCK
    WallpaperType.BOTH -> WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK
}