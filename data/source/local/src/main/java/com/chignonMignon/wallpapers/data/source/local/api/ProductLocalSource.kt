package com.chignonMignon.wallpapers.data.source.local.api

import com.chignonMignon.wallpapers.data.model.domain.Product

interface ProductLocalSource {

    suspend fun getProducts(): List<Product>

    suspend fun saveProducts(products: List<Product>)
}