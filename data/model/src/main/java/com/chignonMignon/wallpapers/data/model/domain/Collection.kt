package com.chignonMignon.wallpapers.data.model.domain

data class Collection(
    val id: String,
    val name: TranslatableText,
    val description: TranslatableText,
    val thumbnailUrl: String,
    val primaryColorCode: String,
    val secondaryColorCode: String,
    val onSecondaryColorCode: String,
    val isPublic: Boolean
)