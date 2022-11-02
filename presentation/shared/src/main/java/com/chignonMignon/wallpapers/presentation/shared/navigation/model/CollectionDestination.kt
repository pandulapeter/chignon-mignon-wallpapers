package com.chignonMignon.wallpapers.presentation.shared.navigation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CollectionDestination(
    val id: String,
    val name: TranslatableTextModel,
    val description: TranslatableTextModel,
    val thumbnailUrl: String,
    val colorPaletteModel: ColorPaletteModel
) : Parcelable