package com.chignonMignon.wallpapers.data.repository.api

import com.chignonMignon.wallpapers.data.model.domain.Wallpaper

interface WallpaperRepository {

    suspend fun getWallpapers(isForceRefresh: Boolean): List<Wallpaper>

    suspend fun getWallpaperById(isForceRefresh: Boolean, wallpaperId: String): Wallpaper

    suspend fun getWallpapersByCollectionId(isForceRefresh: Boolean, collectionId: String): List<Wallpaper>
}