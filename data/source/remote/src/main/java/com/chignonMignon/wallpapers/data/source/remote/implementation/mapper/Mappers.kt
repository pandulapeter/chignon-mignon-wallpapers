package com.chignonMignon.wallpapers.data.source.remote.implementation.mapper

import com.chignonMignon.wallpapers.data.source.remote.implementation.model.exception.DataValidationException

private val hexadecimalStringRegex by lazy { Regex("-?[0-9a-fA-F]+") }

internal fun String?.toColor() = if (this == null || isNullOrBlank()) {
    ""
} else if (length == 7 && first() == '#' && takeLast(6).matches(hexadecimalStringRegex)) {
    this
} else throw DataValidationException("Invalid color code '$this'")

internal fun Boolean?.toBoolean() = this == true