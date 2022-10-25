package com.chignonMignon.wallpapers.presentation.utilities

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import coil.load
import com.chignonMignon.wallpapers.data.model.domain.TranslatableText
import com.chignonMignon.wallpapers.presentation.feature.Navigator
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Locale

inline fun <reified T : Fragment> FragmentManager.handleReplace(
    @IdRes containerId: Int,
    addToBackStack: Boolean = false,
    tag: String = T::class.java.name,
    crossinline newInstance: () -> T
) {
    beginTransaction().apply {
        findFragmentById(containerId)?.run {
            exitTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true)
            reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, false)
        }
        val newFragment = findFragmentByTag(tag) ?: newInstance()
        newFragment.enterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true)
        newFragment.returnTransition = MaterialSharedAxis(MaterialSharedAxis.Y, false)
        replace(containerId, newFragment, tag)
        if (addToBackStack) {
            addToBackStack(null)
        }
        setReorderingAllowed(true)
        commitAllowingStateLoss()
    }
}

internal fun Context.color(@ColorRes colorResourceId: Int) = ContextCompat.getColor(this, colorResourceId)

internal fun Context.dimension(@DimenRes dimensionResourceId: Int) = resources.getDimensionPixelSize(dimensionResourceId)

internal val Fragment.navigator get() = activity as? Navigator

internal inline fun <reified B : ViewDataBinding> Fragment.bind(view: View? = null): B =
    DataBindingUtil.bind<B>(view ?: this.view ?: throw IllegalStateException("Fragment doesn't have a View."))?.apply {
        lifecycleOwner = viewLifecycleOwner
    } ?: throw IllegalStateException("No ViewDataBinding of instance: ${B::class} bound to the Fragment's View.")

internal inline fun <T : Fragment> T.withArguments(bundleOperations: (Bundle) -> Unit): T = apply {
    arguments = Bundle().apply { bundleOperations(this) }
}

internal inline fun <T> Flow<T>.observe(lifecycleOwner: LifecycleOwner, crossinline callback: (T) -> Unit) =
    onEach { callback(it) }.launchIn(lifecycleOwner.lifecycle.coroutineScope)

internal fun <T> MutableSharedFlow<T>.pushEvent(event: T) {
    tryEmit(event)
}

internal fun TranslatableText.toNavigatorTranslatableText() = Navigator.TranslatableText(
    english = english,
    hungarian = hungarian,
    romanian = romanian
)

private fun Navigator.TranslatableText.toText() = when (Locale.getDefault().language) {
    "hu" -> hungarian
    "ro" -> romanian
    else -> english
}

@BindingAdapter("android:text")
internal fun TextView.setText(translatableText: Navigator.TranslatableText?) {
    text = translatableText?.toText()
}

@BindingAdapter("title")
internal fun Toolbar.setTitle(translatableText: Navigator.TranslatableText?) {
    title = translatableText?.toText()
}

@BindingAdapter("imageUrl")
internal fun ImageView.setImageUrl(imageUrl: String?) = load(imageUrl) {
    allowHardware(false)
}