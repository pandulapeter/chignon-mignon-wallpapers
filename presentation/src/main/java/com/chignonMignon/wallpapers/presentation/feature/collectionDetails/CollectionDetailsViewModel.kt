package com.chignonMignon.wallpapers.presentation.feature.collectionDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chignonMignon.wallpapers.data.model.Result
import com.chignonMignon.wallpapers.data.model.domain.Wallpaper
import com.chignonMignon.wallpapers.domain.useCases.GetWallpapersByCollectionIdUseCase
import com.chignonMignon.wallpapers.presentation.feature.Navigator
import com.chignonMignon.wallpapers.presentation.feature.collectionDetails.list.CollectionDetailsListItem
import com.chignonMignon.wallpapers.presentation.utilities.ColorPaletteGenerator
import com.chignonMignon.wallpapers.presentation.utilities.eventFlow
import com.chignonMignon.wallpapers.presentation.utilities.extensions.pushEvent
import com.chignonMignon.wallpapers.presentation.utilities.toNavigatorColorPalette
import com.chignonMignon.wallpapers.presentation.utilities.toNavigatorTranslatableText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class CollectionDetailsViewModel(
    val collection: Navigator.Collection,
    private val getWallpapersByCollectionId: GetWallpapersByCollectionIdUseCase,
    private val colorPaletteGenerator: ColorPaletteGenerator
) : ViewModel() {

    private val _events = eventFlow<Event>()
    val events: Flow<Event> = _events
    private val wallpapers = MutableStateFlow<List<Navigator.Wallpaper>?>(null)
    val items = wallpapers.map { wallpapers -> if (wallpapers.isNullOrEmpty()) emptyList() else wallpapers.map { CollectionDetailsListItem.WallpaperUiModel(it) } }
    private val _shouldShowLoadingIndicator = MutableStateFlow(false)
    val shouldShowLoadingIndicator: StateFlow<Boolean> = _shouldShowLoadingIndicator
    private val _shouldShowErrorState = MutableStateFlow(false)
    val shouldShowErrorState: StateFlow<Boolean> = _shouldShowErrorState
    val shouldShowEmptyState = combine(wallpapers, shouldShowLoadingIndicator) { wallpapers, shouldShowLoadingIndicator ->
        wallpapers?.isEmpty() == true && !shouldShowLoadingIndicator
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    init {
        loadData(false)
    }

    fun loadData(isForceRefresh: Boolean) = viewModelScope.launch {
        if (!_shouldShowLoadingIndicator.value) {
            _shouldShowErrorState.value = false
            _shouldShowLoadingIndicator.value = true
            when (val result = getWallpapersByCollectionId(isForceRefresh, collection.id)) {
                is Result.Success -> {
                    wallpapers.value = result.data.map { it.toNavigatorWallpaper() }
                    _shouldShowLoadingIndicator.value = false
                }
                is Result.Failure -> {
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

    fun onItemSelected(wallpaperId: String) = wallpapers.value?.firstOrNull { it.id == wallpaperId }?.let {
        _events.pushEvent(Event.OpenWallpaperDetails(it))
    }

    private suspend fun Wallpaper.toNavigatorWallpaper() = colorPaletteGenerator.generateColors(url).let { colorPalette ->
        Navigator.Wallpaper(
            id = id,
            name = name.toNavigatorTranslatableText(),
            description = description.toNavigatorTranslatableText(),
            url = url,
            collectionName = collection.name,
            collectionThumbnailUrl = collection.thumbnailUrl,
            colorPalette = colorPalette.toNavigatorColorPalette()
        )
    }

    sealed class Event {
        object NavigateBack : Event()
        data class OpenWallpaperDetails(val wallpaper: Navigator.Wallpaper) : Event()
        object ShowErrorMessage : Event()
    }
}