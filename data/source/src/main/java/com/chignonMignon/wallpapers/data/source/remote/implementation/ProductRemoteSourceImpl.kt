package com.chignonMignon.wallpapers.data.source.remote.implementation

import com.chignonMignon.wallpapers.data.source.remote.api.ProductRemoteSource
import com.chignonMignon.wallpapers.data.source.remote.implementation.mapper.toModel
import com.chignonMignon.wallpapers.data.source.remote.implementation.networking.NetworkingService

internal class ProductRemoteSourceImpl(
    private val networkingService: NetworkingService
) : ProductRemoteSource {

    override suspend fun getProducts() = networkingService.getProducts().mapNotNull { it.toModel() }.distinctBy { it.id }
}