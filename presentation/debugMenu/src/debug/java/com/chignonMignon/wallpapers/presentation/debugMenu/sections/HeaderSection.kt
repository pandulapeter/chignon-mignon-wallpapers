package com.chignonMignon.wallpapers.presentation.debugMenu.sections

import com.pandulapeter.beagle.modules.DividerModule
import com.pandulapeter.beagle.modules.HeaderModule

internal fun createHeaderSection(
    applicationTitle: String,
    versionName: String,
    versionCode: Int
) = listOf(
    HeaderModule(
        title = applicationTitle,
        subtitle = "v$versionName ($versionCode)"
    ),
    DividerModule()
)