package com.chignonMignon.wallpapers.presentation.utilities.extensions

import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlin.math.roundToInt

data class ImageViewTag(
    val url: String? = null, val bitmap: Bitmap? = null, val loadingIndicator: View? = null
)

val ImageView.imageViewTag get() = tag as? ImageViewTag

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

@BindingAdapter("indicatorColor")
fun CircularProgressIndicator.setTint(color: Int?) = color?.let { setIndicatorColor(color) }

@BindingAdapter(
    value = [
        "marginWithInsetLeft",
        "marginWithInsetTop",
        "marginWithInsetRight",
        "marginWithInsetBottom"
    ],
    requireAll = false
)
fun View.setMarginInsets(
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

@BindingAdapter(
    value = [
        "paddingLeftWithTopOffset",
        "paddingRightWithTopOffset",
        "paddingRightWithRightOffset",
        "paddingBottomWithBottomOffset",
        "horizontalPaddingWithInset",
        "verticalPaddingWithInset"
    ],
    requireAll = false
)
fun View.setPaddingInsets(
    paddingLeftWithTopOffset: Float? = null,
    paddingRightWithTopOffset: Float? = null,
    paddingRightWithRightOffset: Float? = null,
    paddingBottomWithBottomOffset: Float? = null,
    horizontalPaddingWithInset: Float? = null,
    verticalPaddingWithInset: Float? = null
) = ViewCompat.setOnApplyWindowInsetsListener(this) { _, windowInsets ->
    layoutParams = (layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
        val systemBarInsets = windowInsets.toInsets()
        setPadding(
            (paddingLeftWithTopOffset ?: horizontalPaddingWithInset)?.let { it.roundToInt() + systemBarInsets.left } ?: paddingLeft,
            (paddingRightWithTopOffset ?: verticalPaddingWithInset)?.let { it.roundToInt() + systemBarInsets.top } ?: paddingTop,
            (paddingRightWithRightOffset ?: horizontalPaddingWithInset)?.let { it.roundToInt() + systemBarInsets.right } ?: paddingRight,
            (paddingBottomWithBottomOffset ?: verticalPaddingWithInset)?.let { it.roundToInt() + systemBarInsets.bottom } ?: paddingBottom
        )
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

@BindingAdapter("expandedTitleMarginStart") fun CollapsingToolbarLayout.updateExpandedTitleMargin(
    expandedTitleMarginStart: Float? = null
) = ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
    setExpandedTitleMarginStart(((expandedTitleMarginStart ?: 0f) + insets.toInsets().left.toFloat()).roundToInt())
    insets
}

@set:BindingAdapter("android:visibility") var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }