package com.chignonMignon.wallpapers.presentation.utilities.extensions

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.Animatable
import android.graphics.drawable.BitmapDrawable
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
import coil.transform.RoundedCornersTransformation
import coil.transition.TransitionTarget
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlin.math.roundToInt

private const val ROUNDED_CORNER_FIX = 0.75f

data class ImageViewTag(
    val url: String? = null, val bitmap: Bitmap? = null, val loadingIndicator: View? = null
)

val ImageView.imageViewTag get() = tag as? ImageViewTag

@BindingAdapter(
    value = [
        "imageUrl",
        "shouldFade",
        "topCornerRadius",
        "bottomCornerRadius",
        "retryCount"
    ],
    requireAll = false
)
fun ImageView.setImageUrl(
    imageUrl: String?,
    shouldFade: Boolean? = null, topCornerRadius: Float? = null,
    bottomCornerRadius: Float? = null,
    retryCount: Int = 3
) {
    fun retryLoading() = postDelayed({ setImageUrl(imageUrl, shouldFade, topCornerRadius, bottomCornerRadius, retryCount - 1) }, 200)

    if (imageUrl?.isNotBlank() == true && imageViewTag?.url != imageUrl) {
        tag = imageViewTag?.copy(url = imageUrl) ?: ImageViewTag(imageUrl)
        imageViewTag?.loadingIndicator?.isVisible = true
        if (shouldFade == true && isLaidOut) {
            context.imageLoader.enqueue(
                ImageRequest.Builder(context).data(imageUrl).allowHardware(false).crossfade(600).target(object : TransitionTarget {

                    override val drawable get() = this@setImageUrl.drawable

                    override val view get() = this@setImageUrl

                    override fun onSuccess(result: Drawable) {
                        val drawable = if (result is Animatable) result else CrossfadeDrawable(this@setImageUrl.drawable, result, durationMillis = 600)
                        setImageDrawable(drawable)
                        (drawable as? Animatable)?.start()
                        imageViewTag?.loadingIndicator?.isVisible = false
                    }

                    override fun onError(error: Drawable?) {
                        tag = imageViewTag?.copy(url = "")
                        if (retryCount > 0) {
                            retryLoading()
                        } else {
                            val drawable = CrossfadeDrawable(this@setImageUrl.drawable, null, durationMillis = 600)
                            setImageDrawable(drawable)
                            (drawable as? Animatable)?.start()
                            imageViewTag?.loadingIndicator?.isVisible = false
                        }
                    }
                }).build()
            )
        } else {
            load(imageUrl) {
                crossfade(250)
                if (topCornerRadius != null || bottomCornerRadius != null) {
                    transformations(
                        RoundedCornersTransformation(
                            topLeft = (topCornerRadius ?: 0f) * ROUNDED_CORNER_FIX,
                            topRight = (topCornerRadius ?: 0f) * ROUNDED_CORNER_FIX,
                            bottomLeft = (bottomCornerRadius ?: 0f) * ROUNDED_CORNER_FIX,
                            bottomRight = (bottomCornerRadius ?: 0f) * ROUNDED_CORNER_FIX
                        )
                    )
                }
                // TODO: fallback(), error()
                listener(onError = { _, _ ->
                    tag = imageViewTag?.copy(url = "")
                    if (retryCount > 0) {
                        retryLoading()
                    } else {
                        imageViewTag?.loadingIndicator?.isVisible = false
                    }
                }, onSuccess = { _, result ->
                    (result.drawable as? BitmapDrawable)?.bitmap?.let {
                        tag = imageViewTag?.copy(bitmap = it)
                    }
                    imageViewTag?.loadingIndicator?.isVisible = false
                })
                allowHardware(false)
            }
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