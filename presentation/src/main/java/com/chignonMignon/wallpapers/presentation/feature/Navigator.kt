package com.chignonMignon.wallpapers.presentation.feature

import android.os.Parcelable
import androidx.annotation.ColorInt
import kotlinx.parcelize.Parcelize

interface Navigator {

    fun navigateToCollections()

    fun navigateToCollectionDetails(collection: Collection)

    fun navigateToWallpaperDetails(wallpaper: Wallpaper)

    fun navigateToAbout()

    fun navigateBack()

    @Parcelize
    data class Collection internal constructor(
        val id: String,
        val name: TranslatableText,
        val description: TranslatableText,
        val thumbnailUrl: String,
        val colorPalette: ColorPalette
    ) : Parcelable

    @Parcelize
    data class Wallpaper internal constructor(
        val id: String,
        val name: TranslatableText,
        val description: TranslatableText,
        val url: String,
        val collectionName: TranslatableText,
        val colorPalette: ColorPalette
    ) : Parcelable

    @Parcelize
    data class TranslatableText internal constructor(
        val english: String,
        val hungarian: String,
        val romanian: String
    ) : Parcelable

    @Parcelize
    data class ColorPalette(
        @ColorInt val background: Int,
        @ColorInt val foreground: Int
    ) : Parcelable
}