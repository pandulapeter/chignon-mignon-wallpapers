package com.chignonMignon.wallpapers.presentation.debugMenu.sections

import com.chignonMignon.wallpapers.presentation.debugMenu.utilities.insetHandler
import com.chignonMignon.wallpapers.presentation.debugMenu.utilities.toast
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule
import com.pandulapeter.beagle.modules.DividerModule
import com.pandulapeter.beagle.modules.KeylineOverlaySwitchModule
import com.pandulapeter.beagle.modules.PaddingModule
import com.pandulapeter.beagle.modules.SwitchModule
import com.pandulapeter.beagle.modules.TextModule

private const val ID_USE_MOCK_DATA = "useMockData"

internal val shouldUseMockData get() = Beagle.find<SwitchModule>(ID_USE_MOCK_DATA)?.getCurrentValue(Beagle) == true

internal fun createTestingSection() = listOf(
    TextModule(
        text = "Testing",
        type = TextModule.Type.SECTION_HEADER
    ),
    KeylineOverlaySwitchModule(
        applyInsets = insetHandler
    ),
    AnimationDurationSwitchModule(),
    SwitchModule(
        id = ID_USE_MOCK_DATA,
        text = "Use mock data",
        onValueChanged = { toast("Refresh the screen to see the changes.") },
        isValuePersisted = true
    ),
    PaddingModule(),
    DividerModule()
)
