package com.chignonMignon.wallpapers.presentation.feature.collectionDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chignonMignon.wallpapers.data.model.Result
import com.chignonMignon.wallpapers.data.model.domain.Wallpaper
import com.chignonMignon.wallpapers.domain.useCases.GetWallpapersByCollectionIdUseCase
import com.chignonMignon.wallpapers.presentation.feature.Navigator
import com.chignonMignon.wallpapers.presentation.utilities.eventFlow
import com.chignonMignon.wallpapers.presentation.utilities.pushEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class CollectionDetailsViewModel(
    val collection: Navigator.Collection,
    private val getWallpapersByCollectionId: GetWallpapersByCollectionIdUseCase
) : ViewModel() {

    private val wallpapers = MutableStateFlow<List<Wallpaper>?>(null)
    private val _shouldShowLoadingIndicator = MutableStateFlow(false)
    val shouldShowLoadingIndicator: StateFlow<Boolean> = _shouldShowLoadingIndicator
    private val _events = eventFlow<Event>()
    val events: Flow<Event> = _events

    init {
        loadData(false)
    }

    fun loadData(isForceRefresh: Boolean) = viewModelScope.launch {
        _shouldShowLoadingIndicator.value = true
        when (val result = getWallpapersByCollectionId(isForceRefresh, collection.id)) {
            is Result.Success -> {
                wallpapers.value = result.data
                _shouldShowLoadingIndicator.value = false
            }
            is Result.Failure -> {
                _events.pushEvent(Event.ShowErrorMessage)
                _shouldShowLoadingIndicator.value = false
            }
        }
    }

    sealed class Event {
        data class OpenWallpaperDetails(val wallpaperId: String) : Event()
        object ShowErrorMessage : Event()
    }
}