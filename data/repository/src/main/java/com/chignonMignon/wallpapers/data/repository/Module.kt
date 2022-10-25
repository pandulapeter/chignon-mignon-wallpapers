package com.chignonMignon.wallpapers.data.repository

import com.chignonMignon.wallpapers.data.repository.api.CollectionRepository
import com.chignonMignon.wallpapers.data.repository.api.WallpaperRepository
import com.chignonMignon.wallpapers.data.repository.implementation.CollectionRepositoryImpl
import com.chignonMignon.wallpapers.data.repository.implementation.WallpaperRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<CollectionRepository> { CollectionRepositoryImpl(get()) }
    single<WallpaperRepository> { WallpaperRepositoryImpl(get()) }
}