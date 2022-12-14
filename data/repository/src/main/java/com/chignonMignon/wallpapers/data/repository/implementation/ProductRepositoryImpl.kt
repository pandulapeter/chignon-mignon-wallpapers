package com.chignonMignon.wallpapers.data.repository.implementation

import com.chignonMignon.wallpapers.data.model.domain.Product
import com.chignonMignon.wallpapers.data.repository.api.ProductRepository
import com.chignonMignon.wallpapers.data.source.local.ProductLocalSource
import com.chignonMignon.wallpapers.data.source.remote.api.ProductRemoteSource

internal class ProductRepositoryImpl(
    productLocalSource: ProductLocalSource,
    productRemoteSource: ProductRemoteSource
) : BaseRepository<List<Product>>(
    getDataFromLocalSource = productLocalSource::getProducts,
    getDataFromRemoteSource = productRemoteSource::getProducts,
    saveDataToLocalSource = productLocalSource::saveProducts
), ProductRepository {

    override fun isDataValid(data: List<Product>) = data.isNotEmpty()

    override suspend fun getProducts(isForceRefresh: Boolean): List<Product> = getData(
        isForceRefresh = isForceRefresh
    )

    override suspend fun getProductsByWallpaperId(isForceRefresh: Boolean, wallpaperId: String) = getProducts(
        isForceRefresh = isForceRefresh
    ).filter { it.wallpaperId == wallpaperId }
}