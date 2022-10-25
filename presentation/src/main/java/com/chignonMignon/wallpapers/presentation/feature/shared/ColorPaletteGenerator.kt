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
        Palette.from(bitmap).generate().lightMutedSwatch?.let { swatch ->
            ColorPalette(
                background = swatch.rgb,
                foreground = swatch.titleTextColor
            )
        }
    } ?: ColorPalette(
        background = defaultBackgroundColor,
        foreground = defaultForegroundColor
    )

    data class ColorPalette(
        @ColorInt val background: Int,
        @ColorInt val foreground: Int
    )
}