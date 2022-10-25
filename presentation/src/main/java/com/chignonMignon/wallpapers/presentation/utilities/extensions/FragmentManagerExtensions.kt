package com.chignonMignon.wallpapers.presentation.utilities.extensions

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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