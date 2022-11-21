package com.chignonMignon.wallpapers.domain.useCases

import com.chignonMignon.wallpapers.data.repository.api.ProductRepository

class GetProductsByWallpaperIdUseCase internal constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke(
        wallpaperId: String
    ) = productRepository.getProductsByWallpaperId(
        wallpaperId = wallpaperId
    )
}