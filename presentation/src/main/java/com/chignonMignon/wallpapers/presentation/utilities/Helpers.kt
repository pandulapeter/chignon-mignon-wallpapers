package com.chignonMignon.wallpapers.presentation.utilities

internal fun consume(action: () -> Any) = true.also { action() }