package com.chignonMignon.wallpapers.presentation.utilities.extensions

import android.view.View
import androidx.annotation.IdRes
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.chignonMignon.wallpapers.presentation.utilities.enterTransition

inline fun <reified T : Fragment> FragmentManager.handleReplace(
    @IdRes containerId: Int,
    addToBackStack: Boolean = false,
    tag: String = T::class.java.name,
    sharedElements: List<View>? = null,
    crossinline newInstance: () -> T
) {
    beginTransaction().apply {
        findFragmentById(containerId)?.run {
            exitTransition = enterTransition(true)
            reenterTransition = enterTransition(false)
        }
        val newFragment = findFragmentByTag(tag) ?: newInstance()
        newFragment.enterTransition = enterTransition(true)
        newFragment.returnTransition = enterTransition(false)
        sharedElements?.forEach { sharedElement -> ViewCompat.getTransitionName(sharedElement)?.let { addSharedElement(sharedElement, it) } }
        replace(containerId, newFragment, tag)
        if (addToBackStack) {
            addToBackStack(null)
        }
        setReorderingAllowed(true)
        commitAllowingStateLoss()
    }
}