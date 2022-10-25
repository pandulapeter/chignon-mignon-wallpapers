package com.chignonMignon.wallpapers.data.model.domain

data class Collection(
    val id: String,
    val name: TranslatableText,
    val description: TranslatableText,
    val thumbnailUrl: String
)