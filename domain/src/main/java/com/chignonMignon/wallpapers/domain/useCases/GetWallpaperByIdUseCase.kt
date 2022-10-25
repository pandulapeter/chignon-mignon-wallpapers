package com.chignonMignon.wallpapers.domain.useCases

import com.chignonMignon.wallpapers.data.repository.api.WallpaperRepository
import com.chignonMignon.wallpapers.domain.resultOf

class GetWallpaperByIdUseCase internal constructor(
    private val wallpaperRepository: WallpaperRepository
) {
    suspend operator fun invoke(
        isForceRefresh: Boolean,
        wallpaperId: String
    ) = resultOf {
        wallpaperRepository.getWallpaperById(
            isForceRefresh = isForceRefresh,
            wallpaperId = wallpaperId
        )
    }
}