package com.chignonMignon.wallpapers.data.repository.api

import com.chignonMignon.wallpapers.data.model.domain.Product

interface ProductRepository {

    suspend fun getProducts(isForceRefresh: Boolean): List<Product>

    suspend fun getProductsByWallpaperId(isForceRefresh: Boolean, wallpaperId: String): List<Product>
}