package com.chignonMignon.wallpapers.presentation.utilities

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.chignonMignon.wallpapers.presentation.feature.Navigator
import com.google.android.material.transition.MaterialSharedAxis

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

internal val Fragment.navigator get() = activity as? Navigator

internal inline fun <reified B : ViewDataBinding> Fragment.bind(view: View? = null): B =
    DataBindingUtil.bind<B>(view ?: this.view ?: throw IllegalStateException("Fragment doesn't have a View."))?.apply {
        lifecycleOwner = viewLifecycleOwner
    } ?: throw IllegalStateException("No ViewDataBinding of instance: ${B::class} bound to the Fragment's View.")

internal inline fun <T : Fragment> T.withArguments(bundleOperations: (Bundle) -> Unit): T = apply {
    arguments = Bundle().apply { bundleOperations(this) }
}