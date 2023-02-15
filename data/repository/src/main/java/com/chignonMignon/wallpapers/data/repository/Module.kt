package com.chignonMignon.wallpapers.data.repository

import com.chignonMignon.wallpapers.data.repository.api.AppStartupRepository
import com.chignonMignon.wallpapers.data.repository.api.CollectionRepository
import com.chignonMignon.wallpapers.data.repository.api.ProductRepository
import com.chignonMignon.wallpapers.data.repository.api.WallpaperRepository
import com.chignonMignon.wallpapers.data.repository.implementation.AppStartupRepositoryImpl
import com.chignonMignon.wallpapers.data.repository.implementation.CollectionRepositoryImpl
import com.chignonMignon.wallpapers.data.repository.implementation.ProductRepositoryImpl
import com.chignonMignon.wallpapers.data.repository.implementation.WallpaperRepositoryImpl
import org.koin.dsl.module

val dataRepositoryModule = module {
    single<AppStartupRepository> { AppStartupRepositoryImpl() }
    single<CollectionRepository> { CollectionRepositoryImpl(get(), get()) }
    single<ProductRepository> { ProductRepositoryImpl(get(), get()) }
    single<WallpaperRepository> { WallpaperRepositoryImpl(get(), get()) }
}