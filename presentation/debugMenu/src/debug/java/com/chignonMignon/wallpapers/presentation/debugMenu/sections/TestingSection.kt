package com.chignonMignon.wallpapers.presentation.debugMenu.sections

import com.chignonMignon.wallpapers.data.model.domain.Collection
import com.chignonMignon.wallpapers.data.model.domain.Wallpaper
import com.chignonMignon.wallpapers.presentation.debugMenu.insetHandler
import com.chignonMignon.wallpapers.presentation.debugMenu.toast
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule
import com.pandulapeter.beagle.modules.DividerModule
import com.pandulapeter.beagle.modules.KeylineOverlaySwitchModule
import com.pandulapeter.beagle.modules.SwitchModule
import com.pandulapeter.beagle.modules.TextModule
import kotlinx.coroutines.delay

private const val ID_USE_MOCK_DATA = "useMockData"
private const val FAKE_LOADING_TIME = 1000L

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
        onValueChanged = { toast("Refresh the screen to see the changes.") }
    ),
    DividerModule()
)

internal suspend fun generateMockCollections(): List<Collection> {
    delay(FAKE_LOADING_TIME)
    return emptyList()
}

internal suspend fun generateMockWallpapers(collectionId: String): List<Wallpaper> {
    delay(FAKE_LOADING_TIME)
    return emptyList()
}