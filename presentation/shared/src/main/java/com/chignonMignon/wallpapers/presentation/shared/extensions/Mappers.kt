package com.chignonMignon.wallpapers.presentation.shared.extensions

import android.graphics.Color
import com.chignonMignon.wallpapers.data.model.domain.TranslatableText
import com.chignonMignon.wallpapers.presentation.shared.navigation.model.TranslatableTextModel
import java.util.Locale

fun TranslatableText.toNavigatorTranslatableText() = TranslatableTextModel(
    english = english,
    hungarian = hungarian,
    romanian = romanian
)

fun String.toNavigatorColorCode() = Color.parseColor(this)

fun TranslatableTextModel.toText() = when (Locale.getDefault().language) {
    "hu" -> hungarian
    "ro" -> romanian
    else -> english
}