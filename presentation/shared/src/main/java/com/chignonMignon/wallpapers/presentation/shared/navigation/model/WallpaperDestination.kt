package com.chignonMignon.wallpapers.presentation.shared.navigation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WallpaperDestination(
    val id: String,
    val name: TranslatableTextModel,
    val thumbnailUrl: String,
    val url: String,
    val colorPaletteModel: ColorPaletteModel,
    val isPublic: Boolean
) : Parcelable