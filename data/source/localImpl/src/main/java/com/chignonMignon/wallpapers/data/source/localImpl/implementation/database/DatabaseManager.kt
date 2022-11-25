package com.chignonMignon.wallpapers.data.source.localImpl.implementation.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chignonMignon.wallpapers.data.source.localImpl.implementation.database.dao.CollectionDao
import com.chignonMignon.wallpapers.data.source.localImpl.implementation.database.dao.ProductDao
import com.chignonMignon.wallpapers.data.source.localImpl.implementation.database.dao.WallpaperDao
import com.chignonMignon.wallpapers.data.source.localImpl.implementation.model.CollectionEntity
import com.chignonMignon.wallpapers.data.source.localImpl.implementation.model.ProductEntity
import com.chignonMignon.wallpapers.data.source.localImpl.implementation.model.WallpaperEntity

@Database(
    entities = [
        CollectionEntity::class,
        ProductEntity::class,
        WallpaperEntity::class
    ],
    version = 1,
    exportSchema = false
)
internal abstract class DatabaseManager : RoomDatabase() {

    abstract fun getCollectionDao(): CollectionDao

    abstract fun getProductDao(): ProductDao

    abstract fun getWallpaperDao(): WallpaperDao
}