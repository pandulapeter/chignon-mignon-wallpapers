package com.chignonMignon.wallpapers.data.repository.implementation

import com.chignonMignon.wallpapers.data.model.domain.Wallpaper
import com.chignonMignon.wallpapers.data.repository.api.WallpaperRepository
import com.chignonMignon.wallpapers.data.source.remote.api.WallpaperRemoteSource

internal class WallpaperRepositoryImpl(
    wallpaperRemoteSource: WallpaperRemoteSource
) : BaseRepository<List<Wallpaper>>(wallpaperRemoteSource::getWallpapers), WallpaperRepository {

    override suspend fun getWallpapers(isForceRefresh: Boolean) = getData(isForceRefresh)
}