package com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import com.chignonMignon.wallpapers.presentation.shared.R

// TODO: Needs to be improved
internal fun Context.setWallpaper(uri: Uri) = Intent.createChooser(
    Intent(Intent.ACTION_SET_WALLPAPER, uri).addCategory(Intent.CATEGORY_DEFAULT).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION),
    getString(R.string.wallpaper_details_set_wallpaper)
).let { intent ->
    val results = packageManager?.queryIntentActivities(intent, PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_ALL.toLong())).orEmpty()
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