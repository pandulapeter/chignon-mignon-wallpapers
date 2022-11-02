package com.chignonMignon.wallpapers.presentation.shared.colorPaletteGenerator

interface ColorPaletteGenerator {

    suspend fun generateColors(url: String): ColorPalette
}