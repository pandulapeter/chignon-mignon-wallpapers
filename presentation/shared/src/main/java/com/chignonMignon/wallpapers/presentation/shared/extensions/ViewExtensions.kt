package com.chignonMignon.wallpapers.presentation.shared.extensions

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.graphics.ColorUtils
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
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
        "bottomCornerRadius"
    ],
    requireAll = false
)
fun ImageView.setImageUrl(
    imageUrl: String?,
    shouldFade: Boolean? = null,
    topCornerRadius: Float? = null,
    bottomCornerRadius: Float? = null
) {
    if (imageUrl?.isNotBlank() == true && imageViewTag?.url != imageUrl) {
        tag = imageViewTag?.copy(url = imageUrl) ?: ImageViewTag(imageUrl)
        post {
            imageViewTag?.run {
                if (!isLoadingReady) {
                    loadingIndicator?.isVisible = true
                }
            }
        }
        Glide.with(context.applicationContext)
            .load(imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .transition(DrawableTransitionOptions.withCrossFade(if (shouldFade == true && isLaidOut) 600 else 250))
            .listener(
                object : RequestListener<Drawable> {
                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        imageViewTag?.loadingIndicator?.isVisible = false
                        tag = imageViewTag?.copy(isLoadingReady = true) ?: ImageViewTag(imageUrl, isLoadingReady = true)
                        scaleType = ImageView.ScaleType.CENTER_CROP
                        return false
                    }

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        scaleType = ImageView.ScaleType.CENTER
                        tag = imageViewTag?.copy(url = "")
                        imageViewTag?.loadingIndicator?.isVisible = false
                        return false
                    }
                }
            )
            .error(R.drawable.img_error)
            .let {
                if (topCornerRadius != null || bottomCornerRadius != null) {
                    it.transform(
                        RoundedCornersTransformation(
                            topCornerRadius ?: 0f,
                            topCornerRadius ?: 0f,
                            bottomCornerRadius ?: 0f,
                            bottomCornerRadius ?: 0f
                        )
                    )
                } else it
            }
            .into(this)
    }
}

private fun View.setOrUpdateGradientDrawable(colorArray: IntArray, orientation: GradientDrawable.Orientation) {
    background = ((background as? GradientDrawable)?.mutate() as? GradientDrawable)?.apply { colors = colorArray } ?: GradientDrawable(orientation, colorArray)
}

private const val TRANSLUCENT_GRADIENT_ALPHA = 192

@BindingAdapter("verticallyFadeTo")
fun View.verticallyFadeTo(color: Int) =
    setOrUpdateGradientDrawable(intArrayOf(ColorUtils.setAlphaComponent(color, TRANSLUCENT_GRADIENT_ALPHA), Color.TRANSPARENT), GradientDrawable.Orientation.BOTTOM_TOP)

@BindingAdapter("horizontallyFadeTo")
fun View.horizontallyFadeTo(color: Int) =
    setOrUpdateGradientDrawable(intArrayOf(ColorUtils.setAlphaComponent(color, TRANSLUCENT_GRADIENT_ALPHA), Color.TRANSPARENT), GradientDrawable.Orientation.RIGHT_LEFT)

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