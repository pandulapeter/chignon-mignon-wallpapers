package com.chignonMignon.wallpapers.data.source.remote.implementation

import com.chignonMignon.wallpapers.data.source.remote.api.WallpaperRemoteSource
import com.chignonMignon.wallpapers.data.source.remote.implementation.mapper.toModel
import com.chignonMignon.wallpapers.data.source.remote.implementation.networking.NetworkingService

internal class WallpaperRemoteSourceImpl(
    private val networkingService: NetworkingService
) : WallpaperRemoteSource {

    override suspend fun getWallpapers() = networkingService.getWallpapers().mapNotNull { it.toModel() }
}