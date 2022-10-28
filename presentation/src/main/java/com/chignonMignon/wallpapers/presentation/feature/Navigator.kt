package com.chignonMignon.wallpapers.presentation.feature

import android.os.Parcelable
import android.view.View
import androidx.annotation.ColorInt
import kotlinx.parcelize.Parcelize

interface Navigator {

    fun navigateToCollections()

    fun navigateToCollectionDetails(collection: Collection, sharedElements: List<View>)

    fun navigateToWallpaperDetails(wallpaper: Wallpaper)

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
        val collectionThumbnailUrl: String,
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
        @ColorInt val primary: Int,
        @ColorInt val secondary: Int,
        @ColorInt val onSecondary: Int
    ) : Parcelable
}