package com.chignonMignon.wallpapers.presentation.shared.navigation.model

import android.os.Parcelable
import androidx.annotation.ColorInt
import kotlinx.parcelize.Parcelize

@Parcelize
data class ColorPaletteModel(
    @ColorInt val primary: Int,
    @ColorInt val secondary: Int
) : Parcelable