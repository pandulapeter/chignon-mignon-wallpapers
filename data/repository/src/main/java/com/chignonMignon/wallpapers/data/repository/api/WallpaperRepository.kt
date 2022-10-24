package com.chignonMignon.wallpapers.data.repository.api

import com.chignonMignon.wallpapers.data.model.domain.Wallpaper

interface WallpaperRepository {

    suspend fun getWallpapers(): List<Wallpaper>
}