package com.chignonMignon.wallpapers.presentation.shared.colorPaletteGenerator

import android.content.Context
import android.graphics.Color
import androidx.core.graphics.ColorUtils
import androidx.palette.graphics.Palette
import com.chignonMignon.wallpapers.presentation.shared.R
import com.chignonMignon.wallpapers.presentation.utilities.extensions.color
import com.chignonMignon.wallpapers.presentation.utilities.extensions.downloadImage

internal class ColorPaletteGeneratorImpl(private val context: Context) : ColorPaletteGenerator {

    private val defaultBackgroundColor by lazy { context.color(R.color.primary) }
    private val defaultForegroundColor by lazy { context.color(R.color.on_primary) }

    override suspend fun generateColors(url: String) = context.downloadImage(url)?.let { bitmap ->
        Palette.from(bitmap).generate().let { palette ->
            val primarySwatch = palette.lightMutedSwatch ?: palette.lightVibrantSwatch ?: palette.vibrantSwatch
            val secondarySwatch = palette.mutedSwatch ?: palette.darkMutedSwatch ?: palette.darkVibrantSwatch
            ColorPalette(
                primary = primarySwatch?.rgb ?: defaultBackgroundColor,
                secondary = ColorUtils.blendARGB(secondarySwatch?.rgb ?: defaultBackgroundColor, Color.WHITE, 0.2f),
                onSecondary = secondarySwatch?.bodyTextColor ?: defaultForegroundColor
            )
        }
    } ?: ColorPalette(
        primary = defaultBackgroundColor,
        secondary = defaultBackgroundColor,
        onSecondary = defaultForegroundColor
    )
}