package com.chignonMignon.wallpapers.domain.useCases

import com.chignonMignon.wallpapers.data.repository.api.WallpaperRepository
import com.chignonMignon.wallpapers.domain.resultOf

class GetWallpapersUseCase internal constructor(
    private val wallpaperRepository: WallpaperRepository
) {
    suspend operator fun invoke() = resultOf { wallpaperRepository.getWallpapers() }
}