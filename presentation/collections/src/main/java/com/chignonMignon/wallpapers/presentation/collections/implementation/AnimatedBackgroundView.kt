package com.chignonMignon.wallpapers.presentation.collections.implementation

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ViewSwitcher
import com.chignonMignon.wallpapers.presentation.collections.R
import com.chignonMignon.wallpapers.presentation.collections.databinding.ViewAnimatedBackgroundBinding
import com.chignonMignon.wallpapers.presentation.shared.extensions.setImageUrl

class AnimatedBackgroundView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ViewSwitcher(context, attrs) {

    private val currentImageView get() = currentView as ImageView
    private val nextImageView get() = nextView as ImageView

    init {
        ViewAnimatedBackgroundBinding.inflate(LayoutInflater.from(context), this)
        inAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_fade_in)
        outAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_fade_out)
        animateFirstView = false
    }

    fun setImageUrl(imageUrl: String?) {
        currentImageView.let { currentImageView ->
            if (currentImageView.tag == null) {
                currentImageView.setImageUrl(imageUrl, false)
            } else {
                nextImageView.setImageUrl(imageUrl, false)
                showNext()
            }
        }
    }
}