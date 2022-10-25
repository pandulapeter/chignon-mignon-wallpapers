package com.chignonMignon.wallpapers.data.model.domain

data class Wallpaper(
    val id: String,
    val collectionId: String,
    val name: TranslatableText,
    val description: TranslatableText,
    val url: String
)