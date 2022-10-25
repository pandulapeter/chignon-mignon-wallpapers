package com.chignonMignon.wallpapers.presentation.utilities

import com.chignonMignon.wallpapers.data.model.domain.TranslatableText
import com.chignonMignon.wallpapers.presentation.feature.Navigator
import java.util.Locale

internal fun TranslatableText.toNavigatorTranslatableText() = Navigator.TranslatableText(
    english = english,
    hungarian = hungarian,
    romanian = romanian
)

internal fun ColorPaletteGenerator.ColorPalette.toNavigatorColorPalette() = Navigator.ColorPalette(
    primary = primary,
    secondary = secondary,
    onSecondary = onSecondary
)

internal fun Navigator.TranslatableText.toText() = when (Locale.getDefault().language) {
    "hu" -> hungarian
    "ro" -> romanian
    else -> english
}
