package com.chignonMignon.wallpapers.presentation.utilities

import android.animation.ValueAnimator
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils

class ColorTransitionManager(
    @ColorInt var defaultColor: Int,
    private val onColorChanged: (color: Int) -> Unit
) {
    private var currentColor = defaultColor

    fun fadeToColor(@ColorInt newColor: Int?, shouldAnimate: Boolean) {
        val newPrimaryColor = newColor ?: defaultColor
        if (shouldAnimate) {
            currentColor.let { currentPrimaryColor ->
                ValueAnimator.ofFloat(0f, 1f).apply {
                    addUpdateListener {
                        onColorChanged(ColorUtils.blendARGB(currentPrimaryColor, newPrimaryColor, it.animatedFraction))
                    }
                }.start()
            }
        } else {
            onColorChanged(newPrimaryColor)
        }
        currentColor = newPrimaryColor
    }
}