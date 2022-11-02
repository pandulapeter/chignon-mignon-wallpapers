package com.chignonMignon.wallpapers.presentation.debugMenu.sections

import com.chignonMignon.wallpapers.data.model.domain.Collection
import com.chignonMignon.wallpapers.data.model.domain.TranslatableText
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
import java.util.UUID
import kotlin.random.Random

private const val ID_USE_MOCK_DATA = "useMockData"
private const val FAKE_LOADING_TIME = 1000L

internal val shouldUseMockData get() = Beagle.find<SwitchModule>(ID_USE_MOCK_DATA)?.getCurrentValue(Beagle) == true
private val random by lazy { Random(System.currentTimeMillis()) }
private var mockCollectionCache: List<Collection>? = null
private var mockWallpaperCache: List<Wallpaper>? = null

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

internal suspend fun generateMockCollections(isForceRefresh: Boolean): List<Collection> {
    if (isForceRefresh || mockCollectionCache == null) {
        delay(FAKE_LOADING_TIME)
    }
    return mockCollectionCache.let { cache ->
        cache ?: (0..generateRandomCount()).map { generateRandomCollection() }.also { mockCollectionCache = it }
    }
}

internal suspend fun generateMockWallpapers(collectionId: String, isForceRefresh: Boolean): List<Wallpaper> {
    if (isForceRefresh || mockWallpaperCache == null) {
        delay(FAKE_LOADING_TIME)
    }
    return mockWallpaperCache.let { cache ->
        (cache ?: (0..generateRandomCount()).map { generateRandomWallpaper() }.also { mockWallpaperCache = it }).map { it.copy(collectionId = collectionId) }
    }
}

private fun generateRandomCount() = random.nextInt(30)

private fun generateRandomIndex() = random.nextInt(200) + 100

private fun generateRandomImage(seed: String) = "https://picsum.photos/seed/${seed}/1080/1080"

private fun generateRandomCollection() = generateRandomIndex().let { index ->
    Collection(
        id = generateRandomId(),
        name = generateRandomName(index),
        description = generateRandomDescription(index),
        thumbnailUrl = generateRandomImage(generateRandomSeed())
    )
}

private fun generateRandomWallpaper() = generateRandomIndex().let { index ->
    Wallpaper(
        id = generateRandomId(),
        collectionId = "",
        name = generateRandomName(index),
        description = generateRandomDescription(index),
        url = generateRandomImage(generateRandomSeed())
    )
}

private fun generateRandomId() = UUID.randomUUID().toString()

private fun generateRandomSeed() = UUID.randomUUID().toString().takeLast(4)

private fun generateRandomName(index: Int) = TranslatableText(
    english = "Name $index",
    hungarian = "Név $index",
    romanian = "Nume $index"
)

private fun generateRandomDescription(index: Int) = TranslatableText(
    english = "Description $index",
    hungarian = "Leírás $index",
    romanian = "Descripție $index"
)