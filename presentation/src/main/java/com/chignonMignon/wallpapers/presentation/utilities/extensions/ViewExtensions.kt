package com.chignonMignon.wallpapers.presentation.utilities.extensions

import android.content.res.ColorStateList
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import coil.drawable.CrossfadeDrawable
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import coil.transition.TransitionTarget
import com.chignonMignon.wallpapers.presentation.feature.Navigator
import com.chignonMignon.wallpapers.presentation.utilities.toText
import com.google.android.material.appbar.CollapsingToolbarLayout

@BindingAdapter("android:text")
internal fun TextView.setText(translatableText: Navigator.TranslatableText?) {
    text = translatableText?.toText()
}

@BindingAdapter("title")
internal fun Toolbar.setTitle(translatableText: Navigator.TranslatableText?) {
    title = translatableText?.toText()
}

@BindingAdapter("title")
internal fun CollapsingToolbarLayout.setTitle(translatableText: Navigator.TranslatableText?) {
    title = translatableText?.toText()
}

@BindingAdapter("subtitle")
internal fun Toolbar.setSubtitle(translatableText: Navigator.TranslatableText?) {
    subtitle = translatableText?.toText()
}

@BindingAdapter(value = ["imageUrl", "shouldFade"], requireAll = false)
internal fun ImageView.setImageUrl(imageUrl: String?, shouldFade: Boolean? = null) = if (shouldFade == true) {
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
            })
            .build()
    )
} else {
    load(imageUrl) { allowHardware(false) }
}

internal fun View.scale(factor: Float) {
    scaleX = factor
    scaleY = factor
}

internal fun View.relativeTranslationX(factor: Float) {
    translationX = width * factor
}

internal fun View.relativeTranslationY(factor: Float) {
    translationY = height * factor
}

@BindingAdapter("tint")
internal fun ImageView.setTint(color: Int?) = color?.let {
    imageTintList = ColorStateList.valueOf(color)
}

@set:BindingAdapter("android:visibility")
internal var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }