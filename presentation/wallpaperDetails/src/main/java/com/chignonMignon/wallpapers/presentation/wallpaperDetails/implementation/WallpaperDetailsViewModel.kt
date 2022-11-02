package com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chignonMignon.wallpapers.presentation.debugMenu.DebugMenu
import com.chignonMignon.wallpapers.presentation.shared.navigation.model.WallpaperDestination
import com.chignonMignon.wallpapers.presentation.utilities.eventFlow
import com.chignonMignon.wallpapers.presentation.utilities.extensions.downloadImage
import com.chignonMignon.wallpapers.presentation.utilities.extensions.getWallpaperFile
import com.chignonMignon.wallpapers.presentation.utilities.extensions.pushEvent
import com.chignonMignon.wallpapers.presentation.utilities.extensions.saveImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class WallpaperDetailsViewModel(
    val wallpaper: WallpaperDestination
) : ViewModel() {
    private val _events = eventFlow<Event>()
    val events: Flow<Event> = _events
    private val _shouldShowLoadingIndicator = MutableStateFlow(false)
    val shouldShowLoadingIndicator: StateFlow<Boolean> = _shouldShowLoadingIndicator

    init {
        DebugMenu.log("Opened wallpaper details for ${wallpaper.id}.")
    }

    fun onSetWallpaperButtonPressed(context: Context) = viewModelScope.launch {
        if (!_shouldShowLoadingIndicator.value) {
            _shouldShowLoadingIndicator.value = true
            DebugMenu.log("Downloading wallpaper: ${wallpaper.id}...")
            delay(1000)
            _events.pushEvent(
                context.downloadImage(wallpaper.url).let { bitmap ->
                    if (bitmap == null) {
                        DebugMenu.log("Error while downloading wallpaper.")
                        Event.ShowErrorMessage
                    } else {
                        context.saveImage(context.getWallpaperFile(wallpaper.id), bitmap).let { uri ->
                            if (uri == null) {
                                DebugMenu.log("Error while saving wallpaper.")
                                Event.ShowErrorMessage
                            } else {
                                DebugMenu.log("Setting wallpaper...")
                                Event.SetWallpaper(uri)
                            }
                        }
                    }
                }
            )
            _shouldShowLoadingIndicator.value = false
        }
    }


    sealed class Event {
        data class SetWallpaper(val uri: Uri) : Event()
        object ShowErrorMessage : Event()
    }
}