package com.chignonMignon.wallpapers.presentation.collectionDetails.implementation

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chignonMignon.wallpapers.data.model.Result
import com.chignonMignon.wallpapers.data.model.domain.Wallpaper
import com.chignonMignon.wallpapers.domain.useCases.GetWallpapersByCollectionIdUseCase
import com.chignonMignon.wallpapers.presentation.collectionDetails.implementation.list.CollectionDetailsListItem
import com.chignonMignon.wallpapers.presentation.debugMenu.DebugMenu
import com.chignonMignon.wallpapers.presentation.shared.colorPaletteGenerator.ColorPaletteGenerator
import com.chignonMignon.wallpapers.presentation.shared.extensions.toNavigatorColorPalette
import com.chignonMignon.wallpapers.presentation.shared.extensions.toNavigatorTranslatableText
import com.chignonMignon.wallpapers.presentation.shared.navigation.model.CollectionDestination
import com.chignonMignon.wallpapers.presentation.shared.navigation.model.WallpaperDestination
import com.chignonMignon.wallpapers.presentation.utilities.eventFlow
import com.chignonMignon.wallpapers.presentation.utilities.extensions.pushEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class CollectionDetailsViewModel(
    val collection: CollectionDestination,
    private val getWallpapersByCollectionId: GetWallpapersByCollectionIdUseCase,
    private val colorPaletteGenerator: ColorPaletteGenerator
) : ViewModel() {
    private val _events = eventFlow<Event>()
    val events: Flow<Event> = _events
    private val wallpapers = MutableStateFlow<List<WallpaperDestination>?>(null)
    val items = wallpapers.map { wallpapers -> if (wallpapers.isNullOrEmpty()) emptyList() else wallpapers.map { CollectionDetailsListItem.WallpaperUiModel(it) } }
    private val _shouldShowLoadingIndicator = MutableStateFlow(false)
    val shouldShowLoadingIndicator: StateFlow<Boolean> = _shouldShowLoadingIndicator
    private val _shouldShowErrorState = MutableStateFlow(false)
    val shouldShowErrorState: StateFlow<Boolean> = _shouldShowErrorState
    val shouldShowEmptyState = combine(wallpapers, shouldShowLoadingIndicator) { wallpapers, shouldShowLoadingIndicator ->
        wallpapers?.isEmpty() == true && !shouldShowLoadingIndicator
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    init {
        DebugMenu.log("Opened collection details for ${collection.id}.")
    }

    fun loadData(isForceRefresh: Boolean) = viewModelScope.launch {
        if (!_shouldShowLoadingIndicator.value) {
            _shouldShowErrorState.value = false
            _shouldShowLoadingIndicator.value = true
            DebugMenu.log("Loading wallpapers (force refresh: $isForceRefresh)...")
            when (val result = DebugMenu.getMockWallpapers(collection.id, isForceRefresh) ?: getWallpapersByCollectionId(isForceRefresh, collection.id)) {
                is Result.Success -> {
                    DebugMenu.log("Loaded ${result.data.size} wallpapers.")
                    wallpapers.value = result.data.map { it.toNavigatorWallpaper() }
                    _shouldShowLoadingIndicator.value = false
                }
                is Result.Failure -> {
                    DebugMenu.log("Failed to load wallpapers: ${result.exception.message}.")
                    if (wallpapers.value == null) {
                        _shouldShowErrorState.value = true
                    } else {
                        _events.pushEvent(Event.ShowErrorMessage)
                    }
                    _shouldShowLoadingIndicator.value = false
                }
            }
        }
    }

    fun onThumbnailClicked() {
        _events.pushEvent(Event.NavigateBack)
    }

    fun onItemSelected(wallpaperId: String, sharedElements: List<View>) = wallpapers.value?.let { wallpapers ->
        wallpapers.indexOfFirst { it.id == wallpaperId }.let { index ->
            if (index != -1) {
                _events.pushEvent(Event.OpenWallpaperDetails(wallpapers, index, sharedElements))
            }
        }
    }

    private suspend fun Wallpaper.toNavigatorWallpaper() = colorPaletteGenerator.generateColors(url).let { colorPalette ->
        WallpaperDestination(
            id = id,
            name = name.toNavigatorTranslatableText(),
            url = url,
            collectionName = collection.name,
            collectionThumbnailUrl = collection.thumbnailUrl,
            colorPaletteModel = colorPalette.toNavigatorColorPalette()
        )
    }

    sealed class Event {
        object NavigateBack : Event()

        data class OpenWallpaperDetails(
            val wallpapers: List<WallpaperDestination>,
            val selectedWallpaperIndex: Int,
            val sharedElements: List<View>
        ) : Event()

        object ShowErrorMessage : Event()
    }
}