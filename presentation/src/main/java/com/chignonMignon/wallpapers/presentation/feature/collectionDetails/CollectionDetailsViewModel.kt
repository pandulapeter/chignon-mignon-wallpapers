package com.chignonMignon.wallpapers.presentation.feature.collectionDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chignonMignon.wallpapers.data.model.Result
import com.chignonMignon.wallpapers.domain.useCases.GetCollectionByIdUseCase
import com.chignonMignon.wallpapers.domain.useCases.GetWallpapersUseCase
import com.chignonMignon.wallpapers.presentation.utilities.eventFlow
import com.chignonMignon.wallpapers.presentation.utilities.pushEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class CollectionDetailsViewModel(
    private val collectionId: String,
    private val getCollectionById: GetCollectionByIdUseCase,
    private val getWallpapers: GetWallpapersUseCase
) : ViewModel() {

    private val _collectionName = MutableStateFlow("")
    val collectionName: StateFlow<String> = _collectionName
    private val _shouldShowLoadingIndicator = MutableStateFlow(false)
    val shouldShowLoadingIndicator: StateFlow<Boolean> = _shouldShowLoadingIndicator
    private val _events = eventFlow<Event>()
    val events: Flow<Event> = _events

    init {
        loadData(false)
    }

    fun loadData(isForceRefresh: Boolean) = viewModelScope.launch {
        _shouldShowLoadingIndicator.value = true
        when (val result = getCollectionById(collectionId)) {
            is Result.Success -> {
                _collectionName.value = result.data.name
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