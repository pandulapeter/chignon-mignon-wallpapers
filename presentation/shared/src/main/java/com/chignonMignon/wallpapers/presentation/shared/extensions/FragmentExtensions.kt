package com.chignonMignon.wallpapers.presentation.shared.extensions

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.chignonMignon.wallpapers.presentation.shared.navigation.Navigator
import com.chignonMignon.wallpapers.presentation.shared.R
import com.google.android.material.snackbar.Snackbar

val Fragment.navigator get() = activity as? Navigator

fun Fragment.showSnackbar(
    @StringRes messageResourceId: Int = R.string.something_went_wrong,
    @StringRes actionButtonTextResourceId: Int = R.string.try_again,
    action: (() -> Unit)? = null
) = showSnackbar(
    message = getString(messageResourceId),
    actionButtonTextResourceId = actionButtonTextResourceId,
    action = action
)

fun Fragment.showSnackbar(
    message: String,
    @StringRes actionButtonTextResourceId: Int = R.string.try_again,
    action: (() -> Unit)? = null
) = view?.run {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).apply {
        action?.let { setAction(actionButtonTextResourceId) { action() } }
    }.show()
}