package com.chignonMignon.wallpapers.presentation.utilities.extensions

import androidx.core.view.WindowInsetsCompat

fun WindowInsetsCompat.toInsets() = getInsetsIgnoringVisibility(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())