package com.chignonMignon.wallpapers.data.source.remote.api

import com.chignonMignon.wallpapers.data.model.domain.Product

interface ProductRemoteSource {

    suspend fun getProducts(): List<Product>
}