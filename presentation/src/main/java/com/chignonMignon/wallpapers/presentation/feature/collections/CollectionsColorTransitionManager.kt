package com.chignonMignon.wallpapers.presentation.feature.collections

import android.animation.ValueAnimator
import android.content.Context
import androidx.core.graphics.ColorUtils
import com.chignonMignon.wallpapers.presentation.R
import com.chignonMignon.wallpapers.presentation.feature.Navigator
import com.chignonMignon.wallpapers.presentation.utilities.extensions.color
import com.chignonMignon.wallpapers.presentation.utilities.extensions.colorResource

internal class CollectionsColorTransitionManager(
    private val context: Context,
    private val updateColors: (primaryColor: Int, secondaryColor: Int, onSecondaryColor: Int) -> Unit
) {
    private var primaryColor: Int? = null
    private var secondaryColor: Int? = null
    private var onSecondaryColor: Int? = null

    fun updateColors(collection: Navigator.Collection?) {
        if (primaryColor == null || secondaryColor == null || onSecondaryColor == null) {
            val windowBackgroundColor = context.colorResource(android.R.attr.windowBackground)
            if (primaryColor == null) {
                primaryColor = windowBackgroundColor
            }
            if (secondaryColor == null) {
                secondaryColor = windowBackgroundColor
            }
            if (onSecondaryColor == null) {
                onSecondaryColor = context.color(R.color.on_primary)
            }
        }
        val newPrimaryColor = collection?.colorPalette?.primary ?: context.color(R.color.primary)
        val newSecondaryColor = collection?.colorPalette?.secondary ?: context.colorResource(android.R.attr.windowBackground)
        val newOnSecondaryColor = collection?.colorPalette?.onSecondary ?: context.color(R.color.on_primary)
        primaryColor?.let { currentPrimaryColor ->
            secondaryColor?.let { currentSecondaryColor ->
                onSecondaryColor?.let { currentOnSecondaryColor ->
                    ValueAnimator.ofFloat(0f, 1f).apply {
                        addUpdateListener {
                            updateColors(
                                ColorUtils.blendARGB(currentPrimaryColor, newPrimaryColor, it.animatedFraction),
                                ColorUtils.blendARGB(currentSecondaryColor, newSecondaryColor, it.animatedFraction),
                                ColorUtils.blendARGB(currentOnSecondaryColor, newOnSecondaryColor, it.animatedFraction)
                            )
                        }
                    }.start()
                    primaryColor = newPrimaryColor
                    secondaryColor = newSecondaryColor
                    onSecondaryColor = newOnSecondaryColor
                }
            }
        }
    }
}