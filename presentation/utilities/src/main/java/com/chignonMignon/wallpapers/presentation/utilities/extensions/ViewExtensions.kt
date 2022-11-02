package com.chignonMignon.wallpapers.presentation.utilities.extensions

import android.content.res.ColorStateList
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import coil.drawable.CrossfadeDrawable
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import coil.transition.TransitionTarget
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlin.math.roundToInt

@BindingAdapter(value = ["imageUrl", "shouldFade"], requireAll = false)
fun ImageView.setImageUrl(imageUrl: String?, shouldFade: Boolean? = null) {
    if (tag != imageUrl) {
        tag = imageUrl
        if (shouldFade == true && isLaidOut) {
            context.imageLoader.enqueue(
                ImageRequest.Builder(context)
                    .data(imageUrl)
                    .allowHardware(false)
                    .crossfade(600)
                    .target(object : TransitionTarget {

                        override val drawable get() = this@setImageUrl.drawable

                        override val view get() = this@setImageUrl

                        override fun onSuccess(result: Drawable) {
                            val drawable = if (result is Animatable) result else CrossfadeDrawable(this@setImageUrl.drawable, result, durationMillis = 600)
                            setImageDrawable(drawable)
                            (drawable as? Animatable)?.start()
                        }

                        override fun onError(error: Drawable?) {
                            val drawable = CrossfadeDrawable(this@setImageUrl.drawable, null, durationMillis = 600)
                            setImageDrawable(drawable)
                            (drawable as? Animatable)?.start()
                        }
                    })
                    .build()
            )
        } else {
            load(imageUrl) { allowHardware(false) }
        }
    }
}

fun View.scale(factor: Float) {
    scaleX = factor
    scaleY = factor
}

fun View.relativeTranslationX(factor: Float) {
    translationX = width * factor
}

fun View.relativeTranslationY(factor: Float) {
    translationY = height * factor
}

@BindingAdapter("tint")
fun ImageView.setTint(color: Int?) = color?.let {
    imageTintList = ColorStateList.valueOf(color)
}

@BindingAdapter(value = ["marginWithInsetLeft", "marginWithInsetTop", "marginWithInsetRight", "marginWithInsetBottom"], requireAll = false)
fun View.setInsets(
    insetLeft: Float? = null,
    insetTop: Float? = null,
    insetRight: Float? = null,
    insetBottom: Float? = null
) = ViewCompat.setOnApplyWindowInsetsListener(this) { _, windowInsets ->
    layoutParams = (layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
        val systemBarInsets = windowInsets.toInsets()
        if (insetLeft != null) {
            leftMargin = (systemBarInsets.left + insetLeft).roundToInt()
        }
        if (insetTop != null) {
            topMargin = (systemBarInsets.top + insetTop).roundToInt()
        }
        if (insetRight != null) {
            rightMargin = (systemBarInsets.right + insetRight).roundToInt()
        }
        if (insetBottom != null) {
            bottomMargin = (systemBarInsets.bottom + insetBottom).roundToInt()
        }
    }
    windowInsets
}

@BindingAdapter("heightWithTopInset")
fun View.setHeightWithTopInset(
    heightWithTopInset: Float? = null
) = ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
    layoutParams = layoutParams?.apply {
        val windowInsets = insets.toInsets()
        if (heightWithTopInset != null) {
            height = (windowInsets.top + heightWithTopInset).roundToInt()
        }
    }
    insets
}

@BindingAdapter("paddingBottomWithBottomOffset")
fun View.setPaddingBottomWithBottomOffset(
    paddingBottomWithBottomOffset: Float? = null
) = ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
    val windowInsets = insets.toInsets()
    if (paddingBottomWithBottomOffset != null) {
        setPadding(paddingLeft, paddingTop, paddingRight, windowInsets.bottom + paddingBottomWithBottomOffset.roundToInt())
    }
    insets
}

@BindingAdapter("expandedTitleMarginStart")
fun CollapsingToolbarLayout.updateExpandedTitleMargin(
    expandedTitleMarginStart: Float? = null
) = ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
    setExpandedTitleMarginStart(((expandedTitleMarginStart ?: 0f) + insets.toInsets().left.toFloat()).roundToInt())
    insets
}

@set:BindingAdapter("android:visibility")
var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }