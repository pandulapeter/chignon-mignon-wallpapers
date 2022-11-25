package com.chignonMignon.wallpapers.data.source.localImpl.implementation

import com.chignonMignon.wallpapers.data.model.domain.Product
import com.chignonMignon.wallpapers.data.source.local.ProductLocalSource
import com.chignonMignon.wallpapers.data.source.localImpl.implementation.database.dao.ProductDao
import com.chignonMignon.wallpapers.data.source.localImpl.implementation.mapper.toEntity
import com.chignonMignon.wallpapers.data.source.localImpl.implementation.mapper.toModel

internal class ProductLocalSourceImpl(
    private val productDao: ProductDao
) : ProductLocalSource {

    override suspend fun getProducts() = productDao.getAll().map { it.toModel() }

    override suspend fun saveProducts(products: List<Product>) = productDao.updateAll(products.map { it.toEntity() })
}