package com.chignonMignon.wallpapers.domain.useCases

import com.chignonMignon.wallpapers.data.repository.api.WallpaperRepository

class AreWallpapersAvailableUseCase internal constructor(
    private val wallpaperRepository: WallpaperRepository
) {
    operator fun invoke() = wallpaperRepository.areWallpapersAvailable()
}