package com.chignonMignon.wallpapers.presentation.utilities.extensions

import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import coil.load
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
internal fun ImageView.setImageUrl(imageUrl: String?, shouldFade: Boolean? = null) = load(imageUrl) {
    allowHardware(false)
    if (shouldFade == true) {
        crossfade(500)
    }
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