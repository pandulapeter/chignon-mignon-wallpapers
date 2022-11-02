package com.chignonMignon.wallpapers.presentation.shared.extensions

import com.chignonMignon.wallpapers.data.model.domain.TranslatableText
import com.chignonMignon.wallpapers.presentation.shared.colorPaletteGenerator.ColorPalette
import com.chignonMignon.wallpapers.presentation.shared.navigation.model.ColorPaletteModel
import com.chignonMignon.wallpapers.presentation.shared.navigation.model.TranslatableTextModel
import java.util.Locale

fun TranslatableText.toNavigatorTranslatableText() = TranslatableTextModel(
    english = english,
    hungarian = hungarian,
    romanian = romanian
)

fun ColorPalette.toNavigatorColorPalette() = ColorPaletteModel(
    primary = primary,
    secondary = secondary,
    onSecondary = onSecondary
)

fun TranslatableTextModel.toText() = when (Locale.getDefault().language) {
    "hu" -> hungarian
    "ro" -> romanian
    else -> english
}