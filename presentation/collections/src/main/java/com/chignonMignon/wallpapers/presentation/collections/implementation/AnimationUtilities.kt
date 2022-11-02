package com.chignonMignon.wallpapers.presentation.collections.implementation

import android.widget.ImageView
import com.chignonMignon.wallpapers.presentation.collections.databinding.ItemCollectionsAboutBinding
import com.chignonMignon.wallpapers.presentation.collections.databinding.ItemCollectionsCollectionBinding
import com.chignonMignon.wallpapers.presentation.collections.databinding.ItemCollectionsWelcomeBinding
import com.chignonMignon.wallpapers.presentation.utilities.extensions.relativeTranslationX
import com.chignonMignon.wallpapers.presentation.utilities.extensions.relativeTranslationY
import com.chignonMignon.wallpapers.presentation.utilities.extensions.scale
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin

internal fun ItemCollectionsAboutBinding.animate(position: Float) {
    root.alpha = 1f - position * position
}

internal fun ItemCollectionsCollectionBinding.animate(position: Float) {
    val multiplier = 1f - abs(position)
    val multiplierSquared = multiplier * multiplier
    thumbnail.run {
        alpha = multiplierSquared
        scale(multiplier)
        relativeTranslationX(-position * 0.5f)
        relativeTranslationY(-sin((1 - multiplier) * PI.toFloat()) * 0.05f)
    }
    name.alpha = multiplierSquared
    description.run {
        relativeTranslationX(position * 0.5f)
        alpha = multiplierSquared
    }
}

internal fun ItemCollectionsWelcomeBinding.animate(position: Float) {
    root.run {
        alpha = 1f + position * 2f
        relativeTranslationX(-position)
    }
    thumbnail.scale(1f + position)
    message.relativeTranslationY(position * 2f)
}

internal fun ImageView.animateCollectionsPreviousButton(adjustedPosition: Float) {
    alpha = adjustedPosition * adjustedPosition
    relativeTranslationX(1 - adjustedPosition)
    scale(adjustedPosition)
}

internal fun ImageView.animateCollectionsNextButton(adjustedPosition: Float) {
    alpha = adjustedPosition * adjustedPosition
    relativeTranslationX(adjustedPosition - 1)
    scale(adjustedPosition)
}

internal fun ImageView.animateCollectionsAboutButton(adjustedPosition: Float) {
    alpha = adjustedPosition * adjustedPosition
    scale(adjustedPosition)
}