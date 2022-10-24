package com.chignonMignon.wallpapers.presentation.feature.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chignonMignon.wallpapers.data.model.Result
import com.chignonMignon.wallpapers.data.model.domain.Collection
import com.chignonMignon.wallpapers.domain.useCases.GetCollectionsUseCase
import com.chignonMignon.wallpapers.presentation.feature.collections.list.CollectionsListItem
import com.chignonMignon.wallpapers.presentation.utilities.eventFlow
import com.chignonMignon.wallpapers.presentation.utilities.pushEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class CollectionsViewModel(
    private val getCollections: GetCollectionsUseCase
) : ViewModel() {

    private val collections = MutableStateFlow<List<Collection>?>(null)
    val items = collections.map { collections ->
        if (collections.isNullOrEmpty()) emptyList() else collections.map { CollectionsListItem.CollectionUiModel(it) }
    }
    private val _shouldShowLoadingIndicator = MutableStateFlow(false)
    val shouldShowLoadingIndicator: StateFlow<Boolean> = _shouldShowLoadingIndicator
    private val _events = eventFlow<Event>()
    val events: Flow<Event> = _events

    init {
        loadData(false)
    }

    fun loadData(isForceRefresh: Boolean) = viewModelScope.launch {
        _shouldShowLoadingIndicator.value = true
        when (val result = getCollections(isForceRefresh)) {
            is Result.Success -> {
                collections.value = result.data
                _shouldShowLoadingIndicator.value = false
            }
            is Result.Failure -> {
                _events.pushEvent(Event.ShowErrorMessage)
                _shouldShowLoadingIndicator.value = false
            }
        }
    }

    fun onItemSelected(collectionId: String) = _events.pushEvent(Event.OpenCollectionDetails(collectionId))

    sealed class Event {
        data class OpenCollectionDetails(val collectionId: String) : Event()
        object ShowErrorMessage : Event()
    }
}