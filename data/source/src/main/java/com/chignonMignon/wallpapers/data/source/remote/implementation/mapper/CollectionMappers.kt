package com.chignonMignon.wallpapers.data.source.remote.implementation.mapper

import com.chignonMignon.wallpapers.data.model.domain.Collection
import com.chignonMignon.wallpapers.data.model.domain.TranslatableText
import com.chignonMignon.wallpapers.data.source.remote.implementation.model.CollectionResponse
import com.chignonMignon.wallpapers.data.source.remote.implementation.model.exception.DataValidationException

internal fun CollectionResponse.toModel() = try {
    Collection(
        id = id.toCollectionId(),
        name = nameEn.toCollectionName(nameHu, nameRo),
        description = descriptionEn.toCollectionDescription(descriptionHu, descriptionRo),
        thumbnailUrl = thumbnailUrl.toCollectionThumbnailUrl(),
        primaryColorCode = primaryColorCode.toColor(),
        secondaryColorCode = secondaryColorCode.toColor(),
        onSecondaryColorCode = onSecondaryColorCode.toColor(),
        isPublic = isPublic.toBoolean()
    )
} catch (exception: DataValidationException) {
    println(exception.message)
    null
}

internal fun String?.toCollectionId() = if (isNullOrBlank()) throw DataValidationException("Missing collection ID.") else this

private fun String?.toCollectionName(
    hungarian: String?,
    romanian: String?
) = if (isNullOrBlank()) throw DataValidationException("Missing collection name.") else TranslatableText(
    english = this,
    hungarian = if (hungarian.isNullOrBlank()) this else hungarian,
    romanian = if (romanian.isNullOrBlank()) this else romanian
)

private fun String?.toCollectionDescription(
    hungarian: String?,
    romanian: String?
) = TranslatableText(
    english = orEmpty(),
    hungarian = if (hungarian.isNullOrBlank()) orEmpty() else hungarian,
    romanian = if (romanian.isNullOrBlank()) orEmpty() else romanian
)

private fun String?.toCollectionThumbnailUrl() = this ?: throw DataValidationException("Missing collection thumbnail URL.")