package com.chignonMignon.wallpapers.presentation.debugMenu

import com.pandulapeter.beagle.modules.BugReportButtonModule
import com.pandulapeter.beagle.modules.DeviceInfoModule
import com.pandulapeter.beagle.modules.DividerModule
import com.pandulapeter.beagle.modules.ScreenCaptureToolboxModule
import com.pandulapeter.beagle.modules.TextModule

fun createGeneralSection() = listOf(
    TextModule(
        text = "General",
        type = TextModule.Type.SECTION_HEADER
    ),
    BugReportButtonModule(),
    ScreenCaptureToolboxModule(),
    DeviceInfoModule(),
    DividerModule()
)