package com.chignonMignon.wallpapers.presentation.feature.shared

import android.content.Context
import androidx.annotation.ColorInt
import androidx.palette.graphics.Palette
import com.chignonMignon.wallpapers.presentation.R
import com.chignonMignon.wallpapers.presentation.utilities.extensions.color
import com.chignonMignon.wallpapers.presentation.utilities.extensions.downloadImage

internal class ColorPaletteGenerator(private val context: Context) {

    private val defaultBackgroundColor by lazy { context.color(R.color.primary) }
    private val defaultForegroundColor by lazy { context.color(R.color.on_primary) }

    suspend fun generateColors(url: String) = context.downloadImage(url)?.let { bitmap ->
        Palette.from(bitmap).generate().let { palette ->
            val primarySwatch = palette.lightMutedSwatch ?: palette.lightVibrantSwatch ?: palette.vibrantSwatch
            val secondarySwatch = palette.darkMutedSwatch ?: palette.darkVibrantSwatch ?: palette.mutedSwatch
            ColorPalette(
                primary = primarySwatch?.rgb ?: defaultBackgroundColor,
                secondary = secondarySwatch?.rgb ?: defaultBackgroundColor,
                onSecondary = secondarySwatch?.bodyTextColor ?: defaultForegroundColor
            )
        }
    } ?: ColorPalette(
        primary = defaultBackgroundColor,
        secondary = defaultBackgroundColor,
        onSecondary = defaultForegroundColor
    )

    data class ColorPalette(
        @ColorInt val primary: Int,
        @ColorInt val secondary: Int,
        @ColorInt val onSecondary: Int
    )
}