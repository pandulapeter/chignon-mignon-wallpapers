package com.chignonMignon.wallpapers.presentation.shared.extensions

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import com.chignonMignon.wallpapers.presentation.shared.R

fun Context.openUrl(url: String, view: View) = try {
    CustomTabsIntent.Builder()
        .setColorScheme(CustomTabsIntent.COLOR_SCHEME_LIGHT)
        .build()
        .launchUrl(this, Uri.parse(url))
} catch (_: ActivityNotFoundException) {
    view.showSnackbar(
        messageResourceId = R.string.no_browser_installed
    )
}

fun Context.openEmailComposer(address: String, view: View) = try {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("mailto:$address")))
} catch (_: ActivityNotFoundException) {
    view.showSnackbar(
        messageResourceId = R.string.no_browser_installed //TODO: error message
    )
}