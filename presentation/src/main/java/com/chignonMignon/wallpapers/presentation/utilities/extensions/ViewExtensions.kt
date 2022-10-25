package com.chignonMignon.wallpapers.presentation.utilities.extensions

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import coil.load
import com.chignonMignon.wallpapers.presentation.feature.Navigator
import com.chignonMignon.wallpapers.presentation.utilities.toText

@BindingAdapter("android:text")
internal fun TextView.setText(translatableText: Navigator.TranslatableText?) {
    text = translatableText?.toText()
}

@BindingAdapter("title")
internal fun Toolbar.setTitle(translatableText: Navigator.TranslatableText?) {
    title = translatableText?.toText()
}

@BindingAdapter("subtitle")
internal fun Toolbar.setSubtitle(translatableText: Navigator.TranslatableText?) {
    subtitle = translatableText?.toText()
}

@BindingAdapter("imageUrl")
internal fun ImageView.setImageUrl(imageUrl: String?) = load(imageUrl) {
    allowHardware(false)
}

@set:BindingAdapter("android:visibility")
internal var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }