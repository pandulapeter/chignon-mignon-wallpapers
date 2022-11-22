package com.chignonMignon.wallpapers.data.source.remote.implementation.mapper

import com.chignonMignon.wallpapers.data.model.domain.Product
import com.chignonMignon.wallpapers.data.source.remote.implementation.model.ProductResponse
import com.chignonMignon.wallpapers.data.source.remote.implementation.model.exception.DataValidationException

internal fun ProductResponse.toModel() = try {
    url.toProductUrl().let { url ->
        Product(
            id = url,
            wallpaperId = wallpaperId.toWallpaperId(),
            thumbnailUrl = thumbnailUrl.toProductThumbnailUrl(),
            url = url,
            isPublic = isPublic.toBoolean()
        )
    }
} catch (exception: DataValidationException) {
    println(exception.message)
    null
}

private fun String?.toProductUrl() = this ?: throw DataValidationException("Missing product URL.")

private fun String?.toProductThumbnailUrl() = this ?: throw DataValidationException("Missing product thumbnail URL.")