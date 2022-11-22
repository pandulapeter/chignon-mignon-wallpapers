package com.chignonMignon.wallpapers.data.repository.implementation

import com.chignonMignon.wallpapers.data.model.domain.Product
import com.chignonMignon.wallpapers.data.repository.api.ProductRepository
import com.chignonMignon.wallpapers.data.source.local.api.ProductLocalSource
import com.chignonMignon.wallpapers.data.source.remote.api.ProductRemoteSource

internal class ProductRepositoryImpl(
    productLocalSource: ProductLocalSource,
    productRemoteSource: ProductRemoteSource
) : BaseRepository<List<Product>>(productRemoteSource::getProducts), ProductRepository {

    override suspend fun getProducts(isForceRefresh: Boolean): List<Product> = getData(isForceRefresh)

    override fun getProductsByWallpaperId(wallpaperId: String) = cache?.filter { it.wallpaperId == wallpaperId } ?: emptyList()
}