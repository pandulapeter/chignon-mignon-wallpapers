package com.chignonMignon.wallpapers.presentation.feature.wallpaperDetails

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chignonMignon.wallpapers.presentation.feature.Navigator
import com.chignonMignon.wallpapers.presentation.utilities.downloadImage
import com.chignonMignon.wallpapers.presentation.utilities.eventFlow
import com.chignonMignon.wallpapers.presentation.utilities.getWallpaperFile
import com.chignonMignon.wallpapers.presentation.utilities.pushEvent
import com.chignonMignon.wallpapers.presentation.utilities.saveImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class WallpaperDetailsViewModel(
    val wallpaper: Navigator.Wallpaper
) : ViewModel() {

    private val _events = eventFlow<Event>()
    val events: Flow<Event> = _events
    private val _shouldShowLoadingIndicator = MutableStateFlow(false)
    val shouldShowLoadingIndicator : StateFlow<Boolean> = _shouldShowLoadingIndicator

    fun onSetWallpaperButtonPressed(context: Context) = viewModelScope.launch {
        if (!_shouldShowLoadingIndicator.value) {
            _shouldShowLoadingIndicator.value = true
            delay(1000)
            _events.pushEvent(
                context.downloadImage(wallpaper.url).let { bitmap ->
                    if (bitmap == null) {
                        Event.ShowErrorMessage
                    } else {
                        context.saveImage(context.getWallpaperFile(wallpaper.id), bitmap).let { uri ->
                            if (uri == null) {
                                Event.ShowErrorMessage
                            } else {
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