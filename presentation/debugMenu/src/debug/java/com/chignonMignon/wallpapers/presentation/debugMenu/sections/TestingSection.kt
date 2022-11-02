package com.chignonMignon.wallpapers.presentation.debugMenu.sections

import com.chignonMignon.wallpapers.presentation.debugMenu.insetHandler
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule
import com.pandulapeter.beagle.modules.DividerModule
import com.pandulapeter.beagle.modules.KeylineOverlaySwitchModule
import com.pandulapeter.beagle.modules.TextModule

internal fun createTestingSection() = listOf(
    TextModule(
        text = "Testing",
        type = TextModule.Type.SECTION_HEADER
    ),
    KeylineOverlaySwitchModule(
        applyInsets = insetHandler
    ),
    AnimationDurationSwitchModule(),
    DividerModule()
)