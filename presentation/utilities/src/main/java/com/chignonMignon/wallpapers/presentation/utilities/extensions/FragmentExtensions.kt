package com.chignonMignon.wallpapers.presentation.utilities.extensions

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.chignonMignon.wallpapers.presentation.utilities.AutoClearedValue

inline fun <reified B : ViewDataBinding> Fragment.bind(view: View? = null): B =
    DataBindingUtil.bind<B>(view ?: this.view ?: throw IllegalStateException("Fragment doesn't have a View."))?.apply {
        lifecycleOwner = viewLifecycleOwner
    } ?: throw IllegalStateException("No ViewDataBinding of instance: ${B::class} bound to the Fragment's View.")

inline fun <T : Fragment> T.withArguments(bundleOperations: (Bundle) -> Unit): T = apply {
    arguments = Bundle().apply { bundleOperations(this) }
}

fun <T : Any> Fragment.autoClearedValue() = AutoClearedValue<T>(this)