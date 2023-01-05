package com.chignonMignon.wallpapers.presentation.shared.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import com.chignonMignon.wallpapers.presentation.shared.R
import com.chignonMignon.wallpapers.presentation.utilities.extensions.dimension

class LogoLoadingIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : FrameLayout(context, attrs) {

    private val interiorImage = AppCompatImageView(context).apply {
        scaleType = ImageView.ScaleType.CENTER_INSIDE
        setImageResource(R.drawable.img_loading_indicator_interior)
        startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_loading_indicator_interior))
    }
    private val exteriorImage = AppCompatImageView(context).apply {
        scaleType = ImageView.ScaleType.CENTER_INSIDE
        setImageResource(R.drawable.img_loading_indicator_exterior)
        startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_loading_indicator_exterior))
    }

    init {
        addView(
            interiorImage,
            context.dimension(R.dimen.logo_loading_indicator_size).let { size -> LayoutParams(size, size) }
        )
        addView(
            exteriorImage,
            context.dimension(R.dimen.logo_loading_indicator_size).let { size -> LayoutParams(size, size) }
        )
    }

    fun setIndicatorColor(@ColorInt color: Int) {

    }
}