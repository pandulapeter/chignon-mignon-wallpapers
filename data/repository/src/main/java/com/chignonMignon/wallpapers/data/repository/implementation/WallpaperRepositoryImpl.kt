package com.chignonMignon.wallpapers.data.repository.implementation

import com.chignonMignon.wallpapers.data.model.domain.Wallpaper
import com.chignonMignon.wallpapers.data.repository.api.WallpaperRepository
import com.chignonMignon.wallpapers.data.source.local.api.WallpaperLocalSource
import com.chignonMignon.wallpapers.data.source.remote.api.WallpaperRemoteSource

internal class WallpaperRepositoryImpl(
    wallpaperLocalSource: WallpaperLocalSource,
    wallpaperRemoteSource: WallpaperRemoteSource
) : BaseRepository<List<Wallpaper>>(
    getDataFromLocalSource = wallpaperLocalSource::getWallpapers,
    getDataFromRemoteSource = wallpaperRemoteSource::getWallpapers,
    saveDataToLocalSource = wallpaperLocalSource::saveWallpapers
), WallpaperRepository {

    override fun isDataValid(data: List<Wallpaper>) = data.isNotEmpty()

    override fun areWallpapersAvailable() = isDataAvailable()

    override suspend fun getWallpapers(isForceRefresh: Boolean) = getData(
        isForceRefresh = isForceRefresh
    )

    override suspend fun getWallpaperById(isForceRefresh: Boolean, wallpaperId: String) = getWallpapers(
        isForceRefresh = isForceRefresh
    ).first { it.id == wallpaperId }

    override suspend fun getWallpapersByCollectionId(isForceRefresh: Boolean, collectionId: String) = getWallpapers(
        isForceRefresh = isForceRefresh
    ).filter { it.collectionId == collectionId }
}