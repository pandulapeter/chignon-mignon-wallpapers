package com.chignonMignon.wallpapers.domain

import com.chignonMignon.wallpapers.domain.useCases.GetCollectionByIdUseCase
import com.chignonMignon.wallpapers.domain.useCases.GetCollectionsUseCase
import com.chignonMignon.wallpapers.domain.useCases.GetWallpapersUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetCollectionByIdUseCase(get()) }
    factory { GetCollectionsUseCase(get()) }
    factory { GetWallpapersUseCase(get { it }) }
}