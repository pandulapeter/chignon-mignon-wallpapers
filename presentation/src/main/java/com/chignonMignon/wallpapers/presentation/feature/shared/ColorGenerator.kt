package com.chignonMignon.wallpapers.presentation.feature.shared

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import androidx.annotation.ColorInt
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.chignonMignon.wallpapers.presentation.R
import com.chignonMignon.wallpapers.presentation.utilities.color

internal class ColorGenerator(private val context: Context) {

    private val defaultBackgroundColor by lazy { context.color(R.color.primary) }
    private val defaultForegroundColor by lazy { context.color(R.color.on_primary) }

    suspend fun generateColors(url: String) = ((ImageLoader(context).execute(
        ImageRequest.Builder(context).data(url).allowHardware(false).build()
    ) as? SuccessResult)?.drawable as? BitmapDrawable)?.bitmap?.let { bitmap ->
        Palette.from(bitmap).generate().dominantSwatch.let { swatch ->
            Colors(
                backgroundColor = swatch?.rgb ?: defaultBackgroundColor,
                foregroundColor = swatch?.titleTextColor ?: defaultForegroundColor
            )
        }
    } ?: Colors(
        backgroundColor = defaultBackgroundColor,
        foregroundColor = defaultForegroundColor
    )

    data class Colors(
        @ColorInt val backgroundColor: Int,
        @ColorInt val foregroundColor: Int
    )
}