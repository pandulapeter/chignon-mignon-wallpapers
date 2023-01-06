package com.chignonMignon.wallpapers.presentation.shared.customViews

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.vectordrawable.graphics.drawable.Animatable2Compat.AnimationCallback
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.chignonMignon.wallpapers.presentation.shared.R
import com.chignonMignon.wallpapers.presentation.shared.navigation.model.ColorPaletteModel
import com.chignonMignon.wallpapers.presentation.utilities.extensions.color

class LogoSwiperRefreshLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : SwipeRefreshLayout(context, attrs) {

    private val imageView = getChildAt(0) as? ImageView
    private val defaultIndicatorColor = context.color(R.color.brand_white)

    init {
        AnimatedVectorDrawableCompat.create(context, R.drawable.avd_logo_animation)?.apply {
            registerAnimationCallback(object : AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable?) = start()
            })
            start()
        }?.let { imageView?.setImageDrawable(it) }
    }

    fun setIndicatorColor(colorPaletteModel: ColorPaletteModel?) {
        imageView?.setBackgroundColor(colorPaletteModel?.primary ?: defaultIndicatorColor)
    }
}