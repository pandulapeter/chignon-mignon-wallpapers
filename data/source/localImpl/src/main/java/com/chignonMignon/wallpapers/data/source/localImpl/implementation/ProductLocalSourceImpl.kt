package com.chignonMignon.wallpapers.data.source.localImpl.implementation

import com.chignonMignon.wallpapers.data.model.domain.Product
import com.chignonMignon.wallpapers.data.source.local.api.ProductLocalSource

internal class ProductLocalSourceImpl(
) : ProductLocalSource {

    override suspend fun getProducts(wallpaperId: String) = TODO("Not yet implemented")

    override suspend fun saveProducts(products: List<Product>) = TODO("Not yet implemented")
}