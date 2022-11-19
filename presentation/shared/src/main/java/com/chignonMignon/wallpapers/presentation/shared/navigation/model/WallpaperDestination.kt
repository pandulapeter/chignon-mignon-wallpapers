package com.chignonMignon.wallpapers.presentation.shared.navigation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WallpaperDestination(
    val id: String,
    val name: TranslatableTextModel,
    val url: String,
    val collectionName: TranslatableTextModel,
    val collectionThumbnailUrl: String,
    val colorPaletteModel: ColorPaletteModel,
    val isPublic: Boolean,
    val isColorPaletteReady: Boolean
) : Parcelable