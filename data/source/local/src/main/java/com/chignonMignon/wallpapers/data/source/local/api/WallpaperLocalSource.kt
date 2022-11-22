package com.chignonMignon.wallpapers.data.source.local.api

import com.chignonMignon.wallpapers.data.model.domain.Wallpaper

interface WallpaperLocalSource {

    suspend fun getWallpapers(collectionId: String): List<Wallpaper>

    suspend fun saveWallpapers(wallpapers: List<Wallpaper>)
}