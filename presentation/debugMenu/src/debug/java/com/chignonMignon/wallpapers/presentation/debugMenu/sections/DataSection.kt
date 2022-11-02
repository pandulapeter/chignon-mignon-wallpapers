package com.chignonMignon.wallpapers.presentation.debugMenu.sections

import com.pandulapeter.beagle.modules.DividerModule
import com.pandulapeter.beagle.modules.TextModule

internal fun createDataSection() = listOf(
    TextModule(
        text = "Data",
        type = TextModule.Type.SECTION_HEADER
    ),
    DividerModule()
)