package com.chignonMignon.wallpapers.presentation.utilities.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

inline fun <T> Flow<T>.observe(lifecycleOwner: LifecycleOwner, crossinline callback: (T) -> Unit) =
    onEach { callback(it) }.launchIn(lifecycleOwner.lifecycle.coroutineScope)

fun <T> MutableSharedFlow<T>.pushEvent(event: T) {
    tryEmit(event)
}