package com.chignonMignon.wallpapers.presentation.debugMenu.sections

import com.pandulapeter.beagle.modules.DividerModule
import com.pandulapeter.beagle.modules.LifecycleLogListModule
import com.pandulapeter.beagle.modules.LogListModule
import com.pandulapeter.beagle.modules.TextModule

internal fun createLogsSection() = listOf(
    TextModule(
        text = "Logs",
        type = TextModule.Type.SECTION_HEADER
    ),
    LogListModule(),
    LifecycleLogListModule(),
    DividerModule()
)