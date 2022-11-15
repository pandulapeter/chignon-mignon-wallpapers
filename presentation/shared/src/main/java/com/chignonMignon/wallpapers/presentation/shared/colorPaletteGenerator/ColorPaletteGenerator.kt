package com.chignonMignon.wallpapers.presentation.shared.colorPaletteGenerator

interface ColorPaletteGenerator {

    suspend fun generateColors(
        imageUrl: String,
        overridePrimaryColorCode: String = "",
        overrideSecondaryColorCode: String = "",
        overrideOnSecondaryColorCode: String = ""
    ): ColorPalette
}