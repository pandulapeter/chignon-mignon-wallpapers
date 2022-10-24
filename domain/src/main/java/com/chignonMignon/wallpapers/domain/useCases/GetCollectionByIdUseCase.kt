package com.chignonMignon.wallpapers.domain.useCases

import com.chignonMignon.wallpapers.data.repository.api.CollectionRepository
import com.chignonMignon.wallpapers.domain.resultOf

class GetCollectionByIdUseCase internal constructor(
    private val collectionRepository: CollectionRepository
) {
    suspend operator fun invoke(collectionId: String) = resultOf { collectionRepository.getCollectionById(collectionId) }
}