package com.chignonMignon.wallpapers.domain

import com.chignonMignon.wallpapers.domain.useCases.AreCollectionsAvailableUseCase
import com.chignonMignon.wallpapers.domain.useCases.GetCollectionByIdUseCase
import com.chignonMignon.wallpapers.domain.useCases.GetCollectionsUseCase
import com.chignonMignon.wallpapers.domain.useCases.GetWallpaperByIdUseCase
import com.chignonMignon.wallpapers.domain.useCases.GetWallpapersByCollectionIdUseCase
import com.chignonMignon.wallpapers.domain.useCases.GetWallpapersUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { AreCollectionsAvailableUseCase(get()) }
    factory { GetCollectionByIdUseCase(get()) }
    factory { GetCollectionsUseCase(get()) }
    factory { GetWallpaperByIdUseCase(get()) }
    factory { GetWallpapersByCollectionIdUseCase(get()) }
    factory { GetWallpapersUseCase(get()) }
}