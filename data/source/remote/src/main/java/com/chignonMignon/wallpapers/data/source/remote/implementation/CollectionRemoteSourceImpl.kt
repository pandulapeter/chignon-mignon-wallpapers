package com.chignonMignon.wallpapers.data.source.remote.implementation

import com.chignonMignon.wallpapers.data.source.remote.api.CollectionRemoteSource
import com.chignonMignon.wallpapers.data.source.remote.implementation.mapper.toModel
import com.chignonMignon.wallpapers.data.source.remote.implementation.networking.NetworkingService

internal class CollectionRemoteSourceImpl(
    private val networkingService: NetworkingService
) : CollectionRemoteSource {

    override suspend fun getCollections() = networkingService.getCollections().mapNotNull { it.toModel() }.distinctBy { it.id }
}