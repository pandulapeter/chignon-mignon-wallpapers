package com.chignonMignon.wallpapers.data.source.localImpl.implementation

import com.chignonMignon.wallpapers.data.model.domain.Wallpaper
import com.chignonMignon.wallpapers.data.source.local.WallpaperLocalSource
import com.chignonMignon.wallpapers.data.source.localImpl.implementation.database.dao.WallpaperDao
import com.chignonMignon.wallpapers.data.source.localImpl.implementation.mapper.toEntity
import com.chignonMignon.wallpapers.data.source.localImpl.implementation.mapper.toModel

internal class WallpaperLocalSourceImpl(
    private val wallpaperDao: WallpaperDao
) : WallpaperLocalSource {

    override suspend fun getWallpapers() = wallpaperDao.getAll().map { it.toModel() }

    override suspend fun saveWallpapers(wallpapers: List<Wallpaper>) = wallpaperDao.updateAll(wallpapers.map { it.toEntity() })
}