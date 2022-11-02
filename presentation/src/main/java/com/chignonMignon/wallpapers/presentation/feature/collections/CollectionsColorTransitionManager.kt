package com.chignonMignon.wallpapers.presentation.feature.collections

import android.animation.ValueAnimator
import android.content.Context
import androidx.core.graphics.ColorUtils
import com.chignonMignon.wallpapers.presentation.R
import com.chignonMignon.wallpapers.presentation.feature.Navigator
import com.chignonMignon.wallpapers.presentation.utilities.extensions.color
import com.chignonMignon.wallpapers.presentation.utilities.extensions.colorResource

internal class CollectionsColorTransitionManager(
    context: Context,
    private val updateColors: (primaryColor: Int, secondaryColor: Int, onPrimaryColor: Int, onSecondaryColor: Int) -> Unit
) {
    private val defaultPrimaryColor = context.color(R.color.primary)
    private var primaryColor = defaultPrimaryColor
    private val defaultSecondaryColor = context.colorResource(android.R.attr.windowBackground)
    private var secondaryColor = defaultSecondaryColor
    private val defaultOnPrimaryColor = context.color(R.color.on_primary)
    private var onPrimaryColor = defaultOnPrimaryColor
    private val defaultOnSecondaryColor = context.colorResource(android.R.attr.textColorPrimary)
    private var onSecondaryColor = defaultOnSecondaryColor

    fun updateColors(collection: Navigator.Collection?, shouldAnimate: Boolean) {
        val newPrimaryColor = collection?.colorPalette?.primary ?: defaultPrimaryColor
        val newSecondaryColor = collection?.colorPalette?.secondary ?: defaultSecondaryColor
        val newOnPrimaryColor = collection?.colorPalette?.onSecondary ?: defaultOnPrimaryColor
        val newOnSecondaryColor = collection?.colorPalette?.onSecondary ?: defaultOnSecondaryColor
        if (shouldAnimate) {
            val currentPrimaryColor = primaryColor
            val currentSecondaryColor = secondaryColor
            val currentOnPrimaryColor = onPrimaryColor
            val currentOnSecondaryColor = onSecondaryColor
            ValueAnimator.ofFloat(0f, 1f).apply {
                addUpdateListener {
                    updateColors(
                        ColorUtils.blendARGB(currentPrimaryColor, newPrimaryColor, it.animatedFraction),
                        ColorUtils.blendARGB(currentSecondaryColor, newSecondaryColor, it.animatedFraction),
                        ColorUtils.blendARGB(currentOnPrimaryColor, newOnPrimaryColor, it.animatedFraction),
                        ColorUtils.blendARGB(currentOnSecondaryColor, newOnSecondaryColor, it.animatedFraction)
                    )
                }
            }.start()
        } else {
            updateColors(
                newPrimaryColor,
                newSecondaryColor,
                newOnPrimaryColor,
                newOnSecondaryColor
            )
        }
        primaryColor = newPrimaryColor
        secondaryColor = newSecondaryColor
        onSecondaryColor = newOnSecondaryColor
    }
}