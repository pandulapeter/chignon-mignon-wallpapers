package com.chignonMignon.wallpapers.data.source.remote.implementation.mapper

import com.chignonMignon.wallpapers.data.model.domain.Wallpaper
import com.chignonMignon.wallpapers.data.source.remote.implementation.model.WallpaperResponse
import com.chignonMignon.wallpapers.data.source.remote.implementation.model.exception.DataValidationException

internal fun WallpaperResponse.toModel() = try {
    Wallpaper(
        id = id.toWallpaperId(),
        collectionId = collectionId.toCollectionId(),
        name = description.toWallpaperName(),
        description = description.toWallpaperDescription(),
        url = url.toWallpaperUrl()
    )
} catch (exception: DataValidationException) {
    println(exception.message)
    null
}

private fun String?.toWallpaperId() = if (isNullOrBlank()) throw DataValidationException("Missing wallpaper ID.") else this

private fun String?.toWallpaperName() = orEmpty()

private fun String?.toWallpaperDescription() = orEmpty()

private fun String?.toWallpaperUrl() = this ?: throw DataValidationException("Missing wallpaper URL.")