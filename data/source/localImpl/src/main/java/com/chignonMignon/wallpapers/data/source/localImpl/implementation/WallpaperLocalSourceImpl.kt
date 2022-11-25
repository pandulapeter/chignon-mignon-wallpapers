package com.chignonMignon.wallpapers.data.source.localImpl.implementation

import com.chignonMignon.wallpapers.data.model.domain.Wallpaper
import com.chignonMignon.wallpapers.data.source.local.api.WallpaperLocalSource

internal class WallpaperLocalSourceImpl(
) : WallpaperLocalSource {

    override suspend fun getWallpapers(collectionId: String) = TODO("Not yet implemented")

    override suspend fun saveWallpapers(wallpapers: List<Wallpaper>) = TODO("Not yet implemented")
}