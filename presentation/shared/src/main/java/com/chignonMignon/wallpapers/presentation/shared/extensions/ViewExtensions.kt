package com.chignonMignon.wallpapers.presentation.shared.extensions

import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import com.chignonMignon.wallpapers.presentation.shared.customViews.AnimatedTitleView
import com.chignonMignon.wallpapers.presentation.shared.navigation.model.TranslatableTextModel
import com.google.android.material.appbar.CollapsingToolbarLayout

@BindingAdapter("android:text")
fun TextView.setText(translatableTextModel: TranslatableTextModel?) {
    text = translatableTextModel?.toText()
}

@BindingAdapter("title")
fun Toolbar.setTitle(translatableTextModel: TranslatableTextModel?) {
    title = translatableTextModel?.toText()
}

@BindingAdapter("title")
fun CollapsingToolbarLayout.setTitle(translatableTextModel: TranslatableTextModel?) {
    title = translatableTextModel?.toText()
}

@BindingAdapter("title")
fun AnimatedTitleView.setTitle(translatableTextModel: TranslatableTextModel?) = setTitle(translatableTextModel?.toText())