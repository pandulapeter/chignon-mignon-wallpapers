package com.chignonMignon.wallpapers.domain.useCases

import com.chignonMignon.wallpapers.data.repository.api.CollectionRepository

class AreCollectionsAvailableUseCase internal constructor(
    private val collectionRepository: CollectionRepository
) {
    operator fun invoke() = collectionRepository.areCollectionsAvailable()
}