package com.chignonMignon.wallpapers.presentation.utilities.extensions

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.chignonMignon.wallpapers.presentation.R
import com.chignonMignon.wallpapers.presentation.feature.Navigator
import com.chignonMignon.wallpapers.presentation.utilities.AutoClearedValue
import com.google.android.material.snackbar.Snackbar

internal val Fragment.navigator get() = activity as? Navigator

internal fun Fragment.showSnackbar(
    @StringRes messageResourceId: Int = R.string.something_went_wrong,
    @StringRes actionButtonTextResourceId: Int = R.string.try_again,
    action: (() -> Unit)? = null
) = showSnackbar(
    message = getString(messageResourceId),
    actionButtonTextResourceId = actionButtonTextResourceId,
    action = action
)

internal fun Fragment.showSnackbar(
    message: String,
    @StringRes actionButtonTextResourceId: Int = R.string.try_again,
    action: (() -> Unit)? = null
) = view?.run {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).apply {
        action?.let { setAction(actionButtonTextResourceId) { action() } }
    }.show()
}

internal inline fun <reified B : ViewDataBinding> Fragment.bind(view: View? = null): B =
    DataBindingUtil.bind<B>(view ?: this.view ?: throw IllegalStateException("Fragment doesn't have a View."))?.apply {
        lifecycleOwner = viewLifecycleOwner
    } ?: throw IllegalStateException("No ViewDataBinding of instance: ${B::class} bound to the Fragment's View.")

internal inline fun <T : Fragment> T.withArguments(bundleOperations: (Bundle) -> Unit): T = apply {
    arguments = Bundle().apply { bundleOperations(this) }
}

internal fun <T : Any> Fragment.autoClearedValue() = AutoClearedValue<T>(this)