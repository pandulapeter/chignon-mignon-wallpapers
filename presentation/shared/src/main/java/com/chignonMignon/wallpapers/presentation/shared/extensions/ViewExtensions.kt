package com.chignonMignon.wallpapers.presentation.shared.extensions

import android.graphics.drawable.Animatable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import coil.drawable.CrossfadeDrawable
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import coil.transition.TransitionTarget
import com.chignonMignon.wallpapers.presentation.shared.R
import com.chignonMignon.wallpapers.presentation.shared.customViews.AnimatedTitleView
import com.chignonMignon.wallpapers.presentation.shared.navigation.model.TranslatableTextModel
import com.chignonMignon.wallpapers.presentation.utilities.extensions.ImageViewTag
import com.chignonMignon.wallpapers.presentation.utilities.extensions.imageViewTag
import com.chignonMignon.wallpapers.presentation.utilities.extensions.isVisible
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar

@BindingAdapter("android:text")
fun TextView.setText(translatableTextModel: TranslatableTextModel?) {
    text = translatableTextModel?.toText()
}

@BindingAdapter("title")
fun CollapsingToolbarLayout.setTitle(translatableTextModel: TranslatableTextModel?) {
    title = translatableTextModel?.toText()
}

@BindingAdapter("title")
fun AnimatedTitleView.setTitle(translatableTextModel: TranslatableTextModel?) = setTitle(translatableTextModel?.toText())

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
    shouldFade: Boolean? = null,
    topCornerRadius: Float? = null,
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
                            topLeft = (topCornerRadius ?: 0f),
                            topRight = (topCornerRadius ?: 0f),
                            bottomLeft = (bottomCornerRadius ?: 0f),
                            bottomRight = (bottomCornerRadius ?: 0f)
                        )
                    )
                }
                fallback(R.drawable.img_error)
                error(R.drawable.img_error)
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

private fun View.setOrUpdateGradientDrawable(colorArray: IntArray, orientation: GradientDrawable.Orientation) {
    background = ((background as? GradientDrawable)?.mutate() as? GradientDrawable)?.apply { colors = colorArray } ?: GradientDrawable(orientation, colorArray)
}

@BindingAdapter(value = ["verticalGradientBackgroundA", "verticalGradientBackgroundB"], requireAll = true)
fun View.setVerticalGradientBackground(colorA: Int, colorB: Int) =
    setOrUpdateGradientDrawable(intArrayOf(colorA, colorB), GradientDrawable.Orientation.BOTTOM_TOP)

@BindingAdapter(value = ["horizontalGradientBackgroundA", "horizontalGradientBackgroundB"], requireAll = true)
fun View.setHorizontalGradientBackground(colorA: Int, colorB: Int) =
    setOrUpdateGradientDrawable(intArrayOf(colorA, colorB), GradientDrawable.Orientation.RIGHT_LEFT)

fun View.showSnackbar(
    @StringRes messageResourceId: Int = R.string.something_went_wrong,
    @StringRes actionButtonTextResourceId: Int = R.string.try_again,
    action: (() -> Unit)? = null
) = showSnackbar(
    message = context.getString(messageResourceId),
    actionButtonTextResourceId = actionButtonTextResourceId,
    action = action
)

fun View.showSnackbar(
    message: String,
    @StringRes actionButtonTextResourceId: Int = R.string.try_again,
    action: (() -> Unit)? = null
) = Snackbar.make(this, message, Snackbar.LENGTH_SHORT).apply {
    action?.let { setAction(actionButtonTextResourceId) { action() } }
}.show()