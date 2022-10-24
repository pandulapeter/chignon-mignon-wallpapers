package com.chignonMignon.wallpapers.data.source.remote.api

import com.chignonMignon.wallpapers.data.model.domain.Wallpaper

interface WallpaperRemoteSource {

    suspend fun getTransactions(): List<Wallpaper>
}