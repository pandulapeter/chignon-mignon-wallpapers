package com.chignonMignon.wallpapers.presentation.debugMenu.utilities

import com.chignonMignon.wallpapers.data.model.domain.Collection
import com.chignonMignon.wallpapers.data.model.domain.TranslatableText
import com.chignonMignon.wallpapers.data.model.domain.Wallpaper
import kotlinx.coroutines.delay
import java.util.UUID
import kotlin.random.Random

internal object MockDataGenerator {

    private const val FAKE_LOADING_TIME = 1000L
    private val random by lazy { Random(System.currentTimeMillis()) }
    private var mockCollectionCache: List<Collection>? = null
    private var mockWallpaperCache: List<Wallpaper>? = null
    private val loremIpsumWords by lazy {
        listOf(
            "dolor",
            "sit",
            "amet",
            "consectetur",
            "adipiscing",
            "elit",
            "praesent",
            "nec",
            "dui",
            "nibh",
            "Cras",
            "mollis",
            "elementum",
            "dignissim",
            "Interdum",
            "et",
            "malesuada",
            "fames",
            "ac",
            "ante",
            "ipsum",
            "primis",
            "in",
            "faucibus",
            "Aliquam",
            "erat",
            "volutpat",
            "cras",
            "egestas",
            "ex",
            "eget",
            "vulputate",
            "lacinia",
            "donec",
            "eu",
            "aliquam",
            "bibendum",
            "lectus"
        )
    }

    suspend fun generateMockCollections(isForceRefresh: Boolean): List<Collection> {
        if (isForceRefresh || mockCollectionCache == null) {
            delay(FAKE_LOADING_TIME)
            mockCollectionCache = null
        }
        return mockCollectionCache.let { cache ->
            cache ?: (0..generateRandomCount()).map { generateRandomCollection() }.also { mockCollectionCache = it }
        }
    }

    suspend fun generateMockWallpapers(collectionId: String, isForceRefresh: Boolean): List<Wallpaper> {
        if (isForceRefresh || mockWallpaperCache == null) {
            delay(FAKE_LOADING_TIME)
            mockWallpaperCache = null
        }
        return mockWallpaperCache.let { cache ->
            (cache ?: (0..generateRandomCount()).map { generateRandomWallpaper() }.also { mockWallpaperCache = it }).map { it.copy(collectionId = collectionId) }
        }
    }

    private fun generateRandomCount(until: Int = 30) = random.nextInt(until)

    private fun generateRandomIndex() = random.nextInt(200) + 100

    private fun generateRandomImage(seed: String) = "https://picsum.photos/seed/${seed}/1080/1080"

    private fun generateRandomCollection() = generateRandomIndex().let { index ->
        Collection(
            id = generateRandomId(),
            name = generateRandomName(index),
            description = generateRandomDescription(index),
            thumbnailUrl = generateRandomImage(generateRandomSeed()),
            primaryColorCode = "",
            secondaryColorCode = "",
            onSecondaryColorCode = ""
        )
    }

    private fun generateRandomWallpaper() = generateRandomIndex().let { index ->
        Wallpaper(
            id = generateRandomId(),
            collectionId = "",
            name = generateRandomName(index),
            url = generateRandomImage(generateRandomSeed()),
            primaryColorCode = ""
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
        english = "Description $index - ${generateLoremIpsum()}.",
        hungarian = "Leírás $index - ${generateLoremIpsum()}.",
        romanian = "Descripție $index - ${generateLoremIpsum()}."
    )

    private fun generateLoremIpsum() = "Lorem ipsum " + loremIpsumWords
        .shuffled()
        .take(generateRandomCount(loremIpsumWords.lastIndex))
        .joinToString(" ")
}