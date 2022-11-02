package com.chignonMignon.wallpapers.presentation.collections.implementation

import android.animation.ValueAnimator
import android.content.Context
import androidx.core.graphics.ColorUtils
import com.chignonMignon.wallpapers.presentation.shared.R
import com.chignonMignon.wallpapers.presentation.shared.navigation.model.CollectionDestination
import com.chignonMignon.wallpapers.presentation.utilities.extensions.color
import com.chignonMignon.wallpapers.presentation.utilities.extensions.colorResource

internal class CollectionsColorTransitionManager(
    context: Context,
    private val updateColors: (primaryColor: Int, secondaryColor: Int, onSecondaryColor: Int) -> Unit
) {
    private val defaultPrimaryColor = context.color(R.color.primary)
    private var primaryColor = defaultPrimaryColor
    private val defaultSecondaryColor = context.colorResource(android.R.attr.windowBackground)
    private var secondaryColor = defaultSecondaryColor
    private val defaultOnSecondaryColor = context.colorResource(android.R.attr.textColorPrimary)
    private var onSecondaryColor = defaultOnSecondaryColor

    fun updateColors(collectionDestination: CollectionDestination?, shouldAnimate: Boolean) {
        val newPrimaryColor = collectionDestination?.colorPaletteModel?.primary ?: defaultPrimaryColor
        val newSecondaryColor = collectionDestination?.colorPaletteModel?.secondary ?: defaultSecondaryColor
        val newOnSecondaryColor = collectionDestination?.colorPaletteModel?.onSecondary ?: defaultOnSecondaryColor
        if (shouldAnimate) {
            val currentPrimaryColor = primaryColor
            val currentSecondaryColor = secondaryColor
            val currentOnSecondaryColor = onSecondaryColor
            ValueAnimator.ofFloat(0f, 1f).apply {
                addUpdateListener {
                    updateColors(
                        ColorUtils.blendARGB(currentPrimaryColor, newPrimaryColor, it.animatedFraction),
                        ColorUtils.blendARGB(currentSecondaryColor, newSecondaryColor, it.animatedFraction),
                        ColorUtils.blendARGB(currentOnSecondaryColor, newOnSecondaryColor, it.animatedFraction)
                    )
                }
            }.start()
        } else {
            updateColors(
                newPrimaryColor,
                newSecondaryColor,
                newOnSecondaryColor
            )
        }
        primaryColor = newPrimaryColor
        secondaryColor = newSecondaryColor
        onSecondaryColor = newOnSecondaryColor
    }
}