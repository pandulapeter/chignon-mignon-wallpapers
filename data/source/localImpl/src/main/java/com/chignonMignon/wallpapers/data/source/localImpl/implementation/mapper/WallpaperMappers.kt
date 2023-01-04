package com.chignonMignon.wallpapers.data.source.localImpl.implementation.mapper

import com.chignonMignon.wallpapers.data.model.domain.TranslatableText
import com.chignonMignon.wallpapers.data.model.domain.Wallpaper
import com.chignonMignon.wallpapers.data.source.localImpl.implementation.model.WallpaperEntity

internal fun WallpaperEntity.toModel() = Wallpaper(
    id = id,
    collectionId = collectionId,
    name = TranslatableText(
        english = nameEn,
        hungarian = nameHu,
        romanian = nameRo
    ),
    thumbnailUrl = thumbnailUrl,
    url = url,
    primaryColorCode = primaryColorCode,
    secondaryColorCode = secondaryColorCode,
    isPublic = isPublic
)

internal fun Wallpaper.toEntity() = WallpaperEntity(
    id = id,
    collectionId = collectionId,
    nameEn = name.english,
    nameHu = name.hungarian,
    nameRo = name.romanian,
    thumbnailUrl = thumbnailUrl,
    url = url,
    primaryColorCode = primaryColorCode,
    secondaryColorCode = secondaryColorCode,
    isPublic = isPublic
)