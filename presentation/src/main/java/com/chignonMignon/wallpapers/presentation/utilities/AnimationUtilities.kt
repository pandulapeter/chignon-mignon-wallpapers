package com.chignonMignon.wallpapers.presentation.utilities

import android.widget.ImageView
import com.chignonMignon.wallpapers.presentation.databinding.FragmentCollectionDetailsBinding
import com.chignonMignon.wallpapers.presentation.databinding.ItemCollectionsAboutBinding
import com.chignonMignon.wallpapers.presentation.databinding.ItemCollectionsCollectionBinding
import com.chignonMignon.wallpapers.presentation.databinding.ItemCollectionsWelcomeBinding
import com.chignonMignon.wallpapers.presentation.utilities.extensions.relativeTranslationX
import com.chignonMignon.wallpapers.presentation.utilities.extensions.relativeTranslationY
import com.chignonMignon.wallpapers.presentation.utilities.extensions.scale
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sin

internal fun ItemCollectionsAboutBinding.animate(position: Float) {
    root.alpha = 1f - position
}

internal fun ItemCollectionsCollectionBinding.animate(position: Float) {
    val multiplier = 1f - abs(position)
    val multiplierSquared = multiplier * multiplier
    thumbnail.run {
        alpha = multiplier
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
    thumbnail.scale(max(0f, 0.6f + position))
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

internal fun FragmentCollectionDetailsBinding.animateHeader(multiplier: Float) {
    overlay.alpha = multiplier
    collectionThumbnail.run {
        scaleX = 1f + multiplier * 5f
        scaleY = 1f + multiplier * 3f
        relativeTranslationX(multiplier)
        alpha = 1f - multiplier
    }
    description.run {
        alpha = 1f - multiplier * 2f
        pivotX = width.toFloat()
        scale(1f - multiplier)
        relativeTranslationX(multiplier * 0.2f)
    }
}