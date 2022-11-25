package com.chignonMignon.wallpapers.domain.useCases

import com.chignonMignon.wallpapers.data.repository.api.ProductRepository
import com.chignonMignon.wallpapers.domain.resultOf

class GetProductsUseCase internal constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(
        isForceRefresh: Boolean
    ) = resultOf {
        productRepository.getProducts(isForceRefresh)
    }
}