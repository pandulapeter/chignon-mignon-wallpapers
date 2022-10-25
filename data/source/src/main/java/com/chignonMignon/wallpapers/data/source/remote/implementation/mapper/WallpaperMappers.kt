package com.chignonMignon.wallpapers.data.source.remote.implementation.mapper

import com.chignonMignon.wallpapers.data.model.domain.TranslatableText
import com.chignonMignon.wallpapers.data.model.domain.Wallpaper
import com.chignonMignon.wallpapers.data.source.remote.implementation.model.WallpaperResponse
import com.chignonMignon.wallpapers.data.source.remote.implementation.model.exception.DataValidationException

internal fun WallpaperResponse.toModel() = try {
    Wallpaper(
        id = id.toWallpaperId(),
        collectionId = collectionId.toCollectionId(),
        name = nameEn.toWallpaperName(nameHu, nameRo),
        description = descriptionEn.toWallpaperDescription(descriptionHu, descriptionRo),
        url = url.toWallpaperUrl()
    )
} catch (exception: DataValidationException) {
    println(exception.message)
    null
}

private fun String?.toWallpaperId() = if (isNullOrBlank()) throw DataValidationException("Missing wallpaper ID.") else this

private fun String?.toWallpaperName(
    hungarian: String?,
    romanian: String?
) = if (isNullOrBlank()) throw DataValidationException("Missing wallpaper ID.") else TranslatableText(
    english = this,
    hungarian = if (hungarian.isNullOrBlank()) this else hungarian,
    romanian = if (romanian.isNullOrBlank()) this else romanian
)

private fun String?.toWallpaperDescription(
    hungarian: String?,
    romanian: String?
) = TranslatableText(
    english = orEmpty(),
    hungarian = if (hungarian.isNullOrBlank()) orEmpty() else hungarian,
    romanian = if (romanian.isNullOrBlank()) orEmpty() else romanian
)

private fun String?.toWallpaperUrl() = this ?: throw DataValidationException("Missing wallpaper URL.")