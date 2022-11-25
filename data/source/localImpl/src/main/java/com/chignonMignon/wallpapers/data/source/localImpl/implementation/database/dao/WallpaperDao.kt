package com.chignonMignon.wallpapers.data.source.localImpl.implementation.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.chignonMignon.wallpapers.data.source.localImpl.implementation.model.WallpaperEntity

@Dao
internal interface WallpaperDao {

    @Query("SELECT * FROM ${WallpaperEntity.TABLE_NAME}")
    suspend fun getAll(): List<WallpaperEntity>

    @Query("DELETE FROM ${WallpaperEntity.TABLE_NAME}")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(Wallpapers: List<WallpaperEntity>)

    @Transaction
    suspend fun updateAll(Wallpapers: List<WallpaperEntity>) {
        deleteAll()
        insertAll(Wallpapers)
    }
}