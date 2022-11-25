package com.chignonMignon.wallpapers.presentation.shared.extensions

import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.chignonMignon.wallpapers.presentation.shared.navigation.Navigator
import com.chignonMignon.wallpapers.presentation.shared.R
import com.google.android.material.snackbar.Snackbar

val Fragment.navigator get() = activity as? Navigator

fun Fragment.showSnackbar(
    anchor: View? = null,
    @StringRes messageResourceId: Int = R.string.something_went_wrong,
    @StringRes actionButtonTextResourceId: Int = R.string.try_again,
    action: (() -> Unit)? = null
) = showSnackbar(
    anchor = anchor,
    message = getString(messageResourceId),
    actionButtonTextResourceId = actionButtonTextResourceId,
    action = action
)

fun Fragment.showSnackbar(
    anchor: View? = null,
    message: String,
    @StringRes actionButtonTextResourceId: Int = R.string.try_again,
    action: (() -> Unit)? = null
) = (anchor ?: view)?.run {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).apply {
        action?.let { setAction(actionButtonTextResourceId) { action() } }
    }.show()
}