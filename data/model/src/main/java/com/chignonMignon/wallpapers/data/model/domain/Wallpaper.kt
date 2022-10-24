package com.chignonMignon.wallpapers.data.model.domain

data class Wallpaper(
    val id: String,
    val collectionId: String,
    val name: String,
    val description: String,
    val url: String
)