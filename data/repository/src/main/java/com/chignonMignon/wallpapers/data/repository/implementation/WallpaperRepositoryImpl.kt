package com.chignonMignon.wallpapers.data.repository.implementation

import com.chignonMignon.wallpapers.data.model.domain.Wallpaper
import com.chignonMignon.wallpapers.data.repository.api.WallpaperRepository
import com.chignonMignon.wallpapers.data.source.local.api.WallpaperLocalSource
import com.chignonMignon.wallpapers.data.source.remote.api.WallpaperRemoteSource

internal class WallpaperRepositoryImpl(
    wallpaperLocalSource: WallpaperLocalSource,
    wallpaperRemoteSource: WallpaperRemoteSource
) : BaseRepository<List<Wallpaper>>(wallpaperRemoteSource::getWallpapers), WallpaperRepository {

    override fun areWallpapersAvailable() = isDataAvailable()

    override suspend fun getWallpapers(isForceRefresh: Boolean) =
        getData(isForceRefresh)

    override suspend fun getWallpaperById(isForceRefresh: Boolean, wallpaperId: String) =
        getWallpapers(isForceRefresh).first { it.id == wallpaperId }

    override suspend fun getWallpapersByCollectionId(isForceRefresh: Boolean, collectionId: String) =
        getWallpapers(isForceRefresh).filter { it.collectionId == collectionId }
}