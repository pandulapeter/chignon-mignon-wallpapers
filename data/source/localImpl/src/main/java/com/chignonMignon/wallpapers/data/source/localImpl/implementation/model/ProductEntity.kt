package com.chignonMignon.wallpapers.data.source.localImpl.implementation.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = ProductEntity.TABLE_NAME)
internal data class ProductEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "wallpaperId") val wallpaperId: String,
    @ColumnInfo(name = "thumbnailUrl") val thumbnailUrl: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "isPublic") val isPublic: Boolean
) {

    companion object {
        const val TABLE_NAME = "products"
    }
}