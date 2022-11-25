package com.chignonMignon.wallpapers.data.source.localImpl.implementation.mapper

import com.chignonMignon.wallpapers.data.model.domain.Product
import com.chignonMignon.wallpapers.data.source.localImpl.implementation.model.ProductEntity

internal fun ProductEntity.toModel() = Product(
    id = id,
    wallpaperId = wallpaperId,
    thumbnailUrl = thumbnailUrl,
    url = url,
    isPublic = isPublic
)

internal fun Product.toEntity() = ProductEntity(
    id = id,
    wallpaperId = wallpaperId,
    thumbnailUrl = thumbnailUrl,
    url = url,
    isPublic = isPublic
)