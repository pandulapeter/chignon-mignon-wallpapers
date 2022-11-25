package com.chignonMignon.wallpapers.data.source.localImpl.implementation.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.chignonMignon.wallpapers.data.source.localImpl.implementation.model.ProductEntity

@Dao
internal interface ProductDao {

    @Query("SELECT * FROM ${ProductEntity.TABLE_NAME}")
    suspend fun getAll(): List<ProductEntity>

    @Query("DELETE FROM ${ProductEntity.TABLE_NAME}")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(Products: List<ProductEntity>)

    @Transaction
    suspend fun updateAll(Products: List<ProductEntity>) {
        deleteAll()
        insertAll(Products)
    }
}