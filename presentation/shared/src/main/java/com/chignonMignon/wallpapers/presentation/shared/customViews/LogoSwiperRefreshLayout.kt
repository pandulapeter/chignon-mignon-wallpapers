package com.chignonMignon.wallpapers.presentation.shared.customViews

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.vectordrawable.graphics.drawable.Animatable2Compat.AnimationCallback
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.chignonMignon.wallpapers.presentation.shared.R

class LogoSwiperRefreshLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : SwipeRefreshLayout(context, attrs) {

    private val animatedVectorDrawable = AnimatedVectorDrawableCompat.create(context, R.drawable.avd_logo_animation)?.apply {
        registerAnimationCallback(object : AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) = start()
        })
        start()
    }

    init {
        animatedVectorDrawable?.let { (getChildAt(0) as? ImageView)?.setImageDrawable(it) }
    }
}