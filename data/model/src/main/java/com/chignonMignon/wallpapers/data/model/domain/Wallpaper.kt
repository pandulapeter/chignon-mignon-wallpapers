package com.chignonMignon.wallpapers.data.model.domain

data class Wallpaper(
    val id: String,
    val collectionId: String,
    val name: TranslatableText,
    val url: String,
    val primaryColorCode: String,
    val secondaryColorCode: String,
    val isPublic: Boolean
)