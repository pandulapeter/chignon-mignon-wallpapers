package com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation

import android.content.Context
import android.net.Uri
import androidx.annotation.ColorInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chignonMignon.wallpapers.presentation.debugMenu.DebugMenu
import com.chignonMignon.wallpapers.presentation.shared.navigation.model.WallpaperDestination
import com.chignonMignon.wallpapers.presentation.utilities.eventFlow
import com.chignonMignon.wallpapers.presentation.utilities.extensions.downloadImage
import com.chignonMignon.wallpapers.presentation.utilities.extensions.getWallpaperFile
import com.chignonMignon.wallpapers.presentation.utilities.extensions.pushEvent
import com.chignonMignon.wallpapers.presentation.utilities.extensions.saveImage
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.list.WallpaperDetailsListItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class WallpaperDetailsViewModel(
    private val wallpapers: List<WallpaperDestination>,
    initialWallpaperIndex: Int
) : ViewModel() {
    private val _events = eventFlow<Event>()
    val events: Flow<Event> = _events
    val wallpaperListItems = wallpapers.map { WallpaperDetailsListItem.WallpaperUiModel(it) }
    private val _shouldShowLoadingIndicator = MutableStateFlow(false)
    val shouldShowLoadingIndicator: StateFlow<Boolean> = _shouldShowLoadingIndicator
    private val _focusedWallpaper = MutableStateFlow(wallpapers[initialWallpaperIndex])
    val focusedWallpaper: StateFlow<WallpaperDestination> = _focusedWallpaper
    private val _primaryColor = MutableStateFlow<Int?>(null)
    val primaryColor: StateFlow<Int?> = _primaryColor
    private val _pagerProgress = MutableStateFlow(getPagerProgress(initialWallpaperIndex))
    val pagerProgress: StateFlow<Float> = _pagerProgress

    init {
        DebugMenu.log("Opened wallpaper details.")
    }

    fun onPageSelected(position: Int) {
        _focusedWallpaper.value = wallpapers[position]
        _pagerProgress.value = getPagerProgress(position)
    }

    fun onSetWallpaperButtonPressed(context: Context, wallpaper: WallpaperDestination) = viewModelScope.launch {
        if (!_shouldShowLoadingIndicator.value) {
            _shouldShowLoadingIndicator.value = true
            DebugMenu.log("Downloading wallpaper: ${wallpaper.id}...")
            delay(1000)
            _events.pushEvent(
                context.downloadImage(wallpaper.url).let { bitmap ->
                    if (bitmap == null) {
                        DebugMenu.log("Error while downloading wallpaper.")
                        Event.ShowErrorMessage(wallpaper)
                    } else {
                        context.saveImage(context.getWallpaperFile(wallpaper.id), bitmap).let { uri ->
                            if (uri == null) {
                                DebugMenu.log("Error while saving wallpaper.")
                                Event.ShowErrorMessage(wallpaper)
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

    fun updatePrimaryColor(@ColorInt primaryColor: Int) {
        _primaryColor.value = primaryColor
    }

    private fun getPagerProgress(position: Int) = if (wallpapers.isNotEmpty()) (position + 1f) / wallpapers.size.toFloat() else 0f

    sealed class Event {
        data class SetWallpaper(val uri: Uri) : Event()
        data class ShowErrorMessage(val wallpaper: WallpaperDestination) : Event()
    }
}