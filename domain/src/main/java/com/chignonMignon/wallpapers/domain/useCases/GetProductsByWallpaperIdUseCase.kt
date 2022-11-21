package com.chignonMignon.wallpapers.domain.useCases

import com.chignonMignon.wallpapers.data.repository.api.ProductRepository
import com.chignonMignon.wallpapers.domain.resultOf

class GetProductsByWallpaperIdUseCase internal constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(
        isForceRefresh: Boolean,
        wallpaperId: String
    ) = resultOf {
        productRepository.getProductsByWallpaperId(
            isForceRefresh = isForceRefresh,
            wallpaperId = wallpaperId
        )
    }
}