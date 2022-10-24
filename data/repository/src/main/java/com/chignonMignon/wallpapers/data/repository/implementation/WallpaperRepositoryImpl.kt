package com.chignonMignon.wallpapers.data.repository.implementation

import com.chignonMignon.wallpapers.data.repository.api.WallpaperRepository
import com.chignonMignon.wallpapers.data.source.remote.api.WallpaperRemoteSource

internal class WallpaperRepositoryImpl(
    private val wallpaperRemoteSource: WallpaperRemoteSource
) : WallpaperRepository {

    override suspend fun getWallpapers() = wallpaperRemoteSource.getTransactions()
}