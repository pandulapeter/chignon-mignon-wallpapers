package com.chignonMignon.wallpapers.data.source.localImpl.implementation.mapper

import com.chignonMignon.wallpapers.data.model.domain.Collection
import com.chignonMignon.wallpapers.data.model.domain.TranslatableText
import com.chignonMignon.wallpapers.data.source.localImpl.implementation.model.CollectionEntity

internal fun CollectionEntity.toModel() = Collection(
    id = id,
    name = TranslatableText(
        english = nameEn,
        hungarian = nameHu,
        romanian = nameRo
    ),
    description = TranslatableText(
        english = descriptionEn,
        hungarian = descriptionHu,
        romanian = descriptionRo
    ),
    thumbnailUrl = thumbnailUrl,
    primaryColorCode = primaryColorCode,
    secondaryColorCode = secondaryColorCode,
    onSecondaryColorCode = onSecondaryColorCode,
    isPublic = isPublic
)

internal fun Collection.toEntity() = CollectionEntity(
    id = id,
    nameEn = name.english,
    nameHu = name.hungarian,
    nameRo = name.romanian,
    descriptionEn = description.english,
    descriptionHu = description.hungarian,
    descriptionRo = description.romanian,
    thumbnailUrl = thumbnailUrl,
    primaryColorCode = primaryColorCode,
    secondaryColorCode = secondaryColorCode,
    onSecondaryColorCode = onSecondaryColorCode,
    isPublic = isPublic
)