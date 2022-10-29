package com.chignonMignon.wallpapers.presentation.utilities

import com.chignonMignon.wallpapers.presentation.databinding.FragmentCollectionDetailsBinding
import com.chignonMignon.wallpapers.presentation.databinding.ItemCollectionsAboutBinding
import com.chignonMignon.wallpapers.presentation.databinding.ItemCollectionsCollectionBinding
import com.chignonMignon.wallpapers.presentation.databinding.ItemCollectionsWelcomeBinding
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
        scaleX = multiplier
        scaleY = multiplier
        translationX = -width * (position * 0.5f)
        translationY = -height * sin((1 - multiplier) * PI.toFloat()) * 0.05f
    }
    name.alpha = multiplierSquared
    description.run {
        translationX = width * (position * 0.5f)
        alpha = multiplierSquared
    }
}

internal fun ItemCollectionsWelcomeBinding.animate(position: Float) {
    root.run {
        alpha = 1f + position * 2f
        translationX = -width * position
    }
    thumbnail.run {
        val scale = max(0f, 0.6f + position)
        scaleX = scale
        scaleY = scale
    }
    message.run {
        translationY = height * position * 2f
    }
}

internal fun FragmentCollectionDetailsBinding.animateHeader(multiplier: Float) {
    overlay.alpha = multiplier
    collectionThumbnail.run {
        scaleX = 1f + multiplier * 5f
        scaleY = 1f + multiplier * 3f
        translationX = width * multiplier
        alpha = 1f - multiplier
    }
    description.run {
        alpha = 1f - multiplier * 2f
        pivotX = width.toFloat()
        scaleX = 1f - multiplier
        scaleY = 1f - multiplier
        translationX = width * multiplier * 0.2f
    }
}