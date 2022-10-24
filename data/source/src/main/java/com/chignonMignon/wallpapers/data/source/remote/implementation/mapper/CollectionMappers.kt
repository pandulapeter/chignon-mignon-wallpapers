package com.chignonMignon.wallpapers.data.source.remote.implementation.mapper

import com.chignonMignon.wallpapers.data.model.domain.Collection
import com.chignonMignon.wallpapers.data.source.remote.implementation.model.CollectionResponse
import com.chignonMignon.wallpapers.data.source.remote.implementation.model.exception.DataValidationException

internal fun CollectionResponse.toModel() = try {
    Collection(
        id = id.toCollectionId(),
        name = name.toCollectionName(),
        description = description.toCollectionDescription(),
        thumbnailUrl = thumbnailUrl.toCollectionThumbnailUrl(),
    )
} catch (exception: DataValidationException) {
    println(exception.message)
    null
}

internal fun String?.toCollectionId() = if (isNullOrBlank()) throw DataValidationException("Missing collection ID.") else this

private fun String?.toCollectionName() = if (isNullOrBlank()) throw DataValidationException("Missing collection name.") else this

private fun String?.toCollectionDescription() = orEmpty()

private fun String?.toCollectionThumbnailUrl() = this ?: throw DataValidationException("Missing collection thumbnail URL.")