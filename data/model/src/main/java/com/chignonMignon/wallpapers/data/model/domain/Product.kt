package com.chignonMignon.wallpapers.data.model.domain

data class Product(
    val id: String,
    val wallpaperId: String,
    val thumbnailUrl: String,
    val url: String,
    val isPublic: Boolean
)