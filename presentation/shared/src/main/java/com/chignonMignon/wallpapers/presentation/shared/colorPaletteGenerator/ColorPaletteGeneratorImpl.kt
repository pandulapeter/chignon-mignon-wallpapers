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

    override suspend fun generateColors(
        imageUrl: String,
        overridePrimaryColorCode: String,
        overrideSecondaryColorCode: String,
        overrideOnSecondaryColorCode: String
    ): ColorPalette {
        val overridePrimaryColor = overridePrimaryColorCode.resolveColor()
        val overrideSecondaryColor = overrideSecondaryColorCode.resolveColor()
        val overrideOnSecondaryColor = overrideOnSecondaryColorCode.resolveColor()
        context.downloadImage(imageUrl).let { bitmap ->
            if (bitmap == null) {
                return ColorPalette(
                    primary = overridePrimaryColor ?: defaultBackgroundColor,
                    secondary = overrideSecondaryColor ?: defaultBackgroundColor,
                    onSecondary = overrideOnSecondaryColor ?: defaultForegroundColor
                )
            } else {
                return Palette.from(bitmap).generate().let { palette ->
                    val primarySwatch = palette.lightMutedSwatch ?: palette.lightVibrantSwatch ?: palette.vibrantSwatch
                    val secondarySwatch = palette.mutedSwatch ?: palette.darkMutedSwatch ?: palette.darkVibrantSwatch
                    ColorPalette(
                        primary = overridePrimaryColor ?: primarySwatch?.rgb ?: defaultBackgroundColor,
                        secondary = overrideSecondaryColor ?: secondarySwatch?.rgb?.lightenColor() ?: defaultBackgroundColor,
                        onSecondary = overrideOnSecondaryColor ?: secondarySwatch?.bodyTextColor ?: defaultForegroundColor
                    )
                }
            }
        }
    }

    private fun Int.lightenColor() = ColorUtils.blendARGB(this, Color.WHITE, 0.2f)

    private fun String.resolveColor() = if (isBlank()) null else Color.parseColor(this)
}