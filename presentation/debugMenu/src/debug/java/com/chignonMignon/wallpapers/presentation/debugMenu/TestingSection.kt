package com.chignonMignon.wallpapers.presentation.debugMenu

import androidx.core.view.WindowInsetsCompat
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.common.configuration.Insets
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule
import com.pandulapeter.beagle.modules.DividerModule
import com.pandulapeter.beagle.modules.KeylineOverlaySwitchModule
import com.pandulapeter.beagle.modules.TextModule

fun createTestingSection() = listOf(
    TextModule(
        text = "Testing",
        type = TextModule.Type.SECTION_HEADER
    ),
    KeylineOverlaySwitchModule(
        applyInsets = {
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
    ),
    AnimationDurationSwitchModule(),
    DividerModule()
)