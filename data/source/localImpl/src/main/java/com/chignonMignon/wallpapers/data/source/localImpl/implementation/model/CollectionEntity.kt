package com.chignonMignon.wallpapers.data.source.localImpl.implementation.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CollectionEntity.TABLE_NAME)
internal data class CollectionEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "nameEn") val nameEn: String,
    @ColumnInfo(name = "nameHu") val nameHu: String,
    @ColumnInfo(name = "nameRo") val nameRo: String,
    @ColumnInfo(name = "descriptionEn") val descriptionEn: String,
    @ColumnInfo(name = "descriptionHu") val descriptionHu: String,
    @ColumnInfo(name = "descriptionRo") val descriptionRo: String,
    @ColumnInfo(name = "thumbnailUrl") val thumbnailUrl: String,
    @ColumnInfo(name = "primaryColorCode") val primaryColorCode: String,
    @ColumnInfo(name = "secondaryColorCode") val secondaryColorCode: String,
    @ColumnInfo(name = "isPublic") val isPublic: Boolean
) {

    companion object {
        const val TABLE_NAME = "collections"
    }
}