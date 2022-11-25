package com.chignonMignon.wallpapers.data.source.localImpl

import androidx.room.Room
import com.chignonMignon.wallpapers.data.source.local.CollectionLocalSource
import com.chignonMignon.wallpapers.data.source.local.ProductLocalSource
import com.chignonMignon.wallpapers.data.source.local.WallpaperLocalSource
import com.chignonMignon.wallpapers.data.source.localImpl.implementation.CollectionLocalSourceImpl
import com.chignonMignon.wallpapers.data.source.localImpl.implementation.ProductLocalSourceImpl
import com.chignonMignon.wallpapers.data.source.localImpl.implementation.WallpaperLocalSourceImpl
import com.chignonMignon.wallpapers.data.source.localImpl.implementation.database.DatabaseManager
import org.koin.dsl.module

val localSourceModule = module {
    single { Room.databaseBuilder(get(), DatabaseManager::class.java, "chignonMignonDatabase.db").build() }
    factory { get<DatabaseManager>().getCollectionDao() }
    factory { get<DatabaseManager>().getProductDao() }
    factory { get<DatabaseManager>().getWallpaperDao() }
    factory<CollectionLocalSource> { CollectionLocalSourceImpl(get()) }
    factory<ProductLocalSource> { ProductLocalSourceImpl(get()) }
    factory<WallpaperLocalSource> { WallpaperLocalSourceImpl(get()) }
}