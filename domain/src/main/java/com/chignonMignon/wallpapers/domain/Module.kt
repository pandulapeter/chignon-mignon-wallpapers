package com.chignonMignon.wallpapers.domain

import com.chignonMignon.wallpapers.domain.useCases.GetCollectionsUseCase
import com.chignonMignon.wallpapers.domain.useCases.GetWallpapersUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetCollectionsUseCase(get()) }
    factory { GetWallpapersUseCase(get { it }) }
}