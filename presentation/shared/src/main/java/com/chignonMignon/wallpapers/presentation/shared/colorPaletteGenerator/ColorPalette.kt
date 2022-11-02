package com.chignonMignon.wallpapers.presentation.shared.colorPaletteGenerator

import androidx.annotation.ColorInt

data class ColorPalette(
    @ColorInt val primary: Int,
    @ColorInt val secondary: Int,
    @ColorInt val onSecondary: Int
)