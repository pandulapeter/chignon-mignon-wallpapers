package com.chignonMignon.wallpapers.presentation.debugMenu.utilities

import android.widget.Toast
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.common.configuration.Insets

internal val insetHandler: (Insets) -> Insets = {
    Beagle.currentActivity?.window?.decorView?.rootWindowInsets?.let { windowInsets ->
        WindowInsetsCompat.toWindowInsetsCompat(windowInsets)
            .getInsetsIgnoringVisibility(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()).let { insets ->
                Insets(
                    left = insets.left,
                    top = insets.top,
                    right = insets.right,
                    bottom = insets.bottom,
                )
            }
    } ?: it
}

internal fun toast(text: String) {
    Beagle.currentActivity?.run {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            try {
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
            } catch (_: Exception) {
            }
        }
    }
}