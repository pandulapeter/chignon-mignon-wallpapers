package com.chignonMignon.wallpapers.presentation.utilities

import android.graphics.Color
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

fun <T> eventFlow() = MutableSharedFlow<T>(
    extraBufferCapacity = 1,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
)

fun sharedElementTransition() = MaterialContainerTransform().apply {
    scrimColor = Color.TRANSPARENT
    fadeMode = MaterialContainerTransform.FADE_MODE_CROSS
}
