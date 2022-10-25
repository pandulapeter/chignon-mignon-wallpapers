package com.chignonMignon.wallpapers.domain.useCases

import com.chignonMignon.wallpapers.data.repository.api.WallpaperRepository
import com.chignonMignon.wallpapers.domain.resultOf

class GetWallpapersByCollectionIdUseCase internal constructor(
    private val wallpaperRepository: WallpaperRepository
) {
    suspend operator fun invoke(
        isForceRefresh: Boolean,
        collectionId: String
    ) = resultOf {
        wallpaperRepository.getWallpapersByCollectionId(
            isForceRefresh = isForceRefresh,
            collectionId = collectionId
        )
    }
}