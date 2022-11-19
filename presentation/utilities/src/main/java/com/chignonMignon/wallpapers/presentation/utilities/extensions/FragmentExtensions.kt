package com.chignonMignon.wallpapers.presentation.utilities.extensions

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.chignonMignon.wallpapers.presentation.utilities.AutoClearedValue
import com.chignonMignon.wallpapers.presentation.utilities.enterTransition
import com.chignonMignon.wallpapers.presentation.utilities.sharedElementTransition

inline fun <reified B : ViewDataBinding> Fragment.bind(view: View? = null): B =
    DataBindingUtil.bind<B>(view ?: this.view ?: throw IllegalStateException("Fragment doesn't have a View."))?.apply {
        lifecycleOwner = viewLifecycleOwner
    } ?: throw IllegalStateException("No ViewDataBinding of instance: ${B::class} bound to the Fragment's View.")

inline fun <T : Fragment> T.withArguments(bundleOperations: (Bundle) -> Unit): T = apply {
    arguments = Bundle().apply { bundleOperations(this) }
}

fun <T : Any> Fragment.autoClearedValue() = AutoClearedValue<T>(this)

fun Fragment.setupTransitions() {
    enterTransition = enterTransition(true)
    exitTransition = enterTransition(true)
    reenterTransition = enterTransition(false)
    returnTransition = enterTransition(false)
    sharedElementEnterTransition = sharedElementTransition()
    sharedElementReturnTransition = sharedElementTransition()
}

fun Fragment.delaySharedElementTransition(waitForPreDrawView: View) {
    postponeEnterTransition()
    (view?.parent as? ViewGroup)?.doOnPreDraw { waitForPreDrawView.postDelayed( { startPostponedEnterTransition() } , 100L) }
}