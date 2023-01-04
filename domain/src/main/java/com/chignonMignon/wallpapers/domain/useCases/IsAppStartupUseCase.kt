package com.chignonMignon.wallpapers.domain.useCases

import com.chignonMignon.wallpapers.data.repository.api.AppStartupRepository

class IsAppStartupUseCase internal constructor(
    private val appStartupRepository: AppStartupRepository
) {
    operator fun invoke() = appStartupRepository.isAppStartup()
}