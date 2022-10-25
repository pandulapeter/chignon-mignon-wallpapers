package com.chignonMignon.wallpapers.presentation.feature.collections

import androidx.annotation.ColorInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chignonMignon.wallpapers.data.model.Result
import com.chignonMignon.wallpapers.data.model.domain.Collection
import com.chignonMignon.wallpapers.domain.useCases.GetCollectionsUseCase
import com.chignonMignon.wallpapers.presentation.feature.Navigator
import com.chignonMignon.wallpapers.presentation.feature.collections.list.CollectionsListItem
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

internal class CollectionsViewModel(
    private val getCollections: GetCollectionsUseCase,
    private val colorPaletteGenerator: ColorPaletteGenerator
) : ViewModel() {

    private val _events = eventFlow<Event>()
    val events: Flow<Event> = _events
    private val collections = MutableStateFlow<List<Navigator.Collection>?>(null)
    val items = collections.map { collections ->
        if (collections.isNullOrEmpty()) emptyList() else collections.map { CollectionsListItem.CollectionUiModel(it) }
    }
    private val _shouldShowLoadingIndicator = MutableStateFlow(false)
    val shouldShowLoadingIndicator: StateFlow<Boolean> = _shouldShowLoadingIndicator
    private val _shouldShowErrorState = MutableStateFlow(false)
    val shouldShowErrorState: StateFlow<Boolean> = _shouldShowErrorState
    val shouldShowEmptyState = combine(collections, shouldShowLoadingIndicator) { collections, shouldShowLoadingIndicator ->
        collections?.isEmpty() == true && !shouldShowLoadingIndicator
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    private val _focusedCollection = MutableStateFlow<Navigator.Collection?>(null)
    val focusedCollection: StateFlow<Navigator.Collection?> = _focusedCollection
    private val _primaryColor = MutableStateFlow<Int?>(null)
    val primaryColor: StateFlow<Int?> = _primaryColor
    private val _secondaryColor = MutableStateFlow<Int?>(null)
    val secondaryColor: StateFlow<Int?> = _secondaryColor
    private val _onSecondaryColor = MutableStateFlow<Int?>(null)
    val onSecondaryColor: StateFlow<Int?> = _onSecondaryColor

    init {
        loadData(false)
    }

    fun loadData(isForceRefresh: Boolean) = viewModelScope.launch {
        if (!_shouldShowLoadingIndicator.value) {
            _shouldShowErrorState.value = false
            _shouldShowLoadingIndicator.value = true
            when (val result = getCollections(isForceRefresh)) {
                is Result.Success -> {
                    collections.value = result.data.map { it.toNavigatorCollection() }
                    _shouldShowLoadingIndicator.value = false
                }
                is Result.Failure -> {
                    if (collections.value == null) {
                        _shouldShowErrorState.value = true
                    } else {
                        _events.pushEvent(Event.ShowErrorMessage)
                    }
                    _shouldShowLoadingIndicator.value = false
                }
            }
        }
    }

    fun onPageSelected(position: Int) {
        collections.value?.let { collections ->
            _focusedCollection.value = if (position >= 0 && position < collections.size) collections[position] else null
        }
    }

    fun updateColors(@ColorInt primaryColor: Int, @ColorInt secondaryColor: Int, @ColorInt onSecondaryColor: Int) {
        _primaryColor.value = primaryColor
        _secondaryColor.value = secondaryColor
        _onSecondaryColor.value = onSecondaryColor
    }

    fun onItemSelected(collectionId: String) = collections.value?.firstOrNull { it.id == collectionId }?.let {
        _events.pushEvent(Event.OpenCollectionDetails(it))
    }

    private suspend fun Collection.toNavigatorCollection() = colorPaletteGenerator.generateColors(thumbnailUrl).let { colorPalette ->
        Navigator.Collection(
            id = id,
            name = name.toNavigatorTranslatableText(),
            description = description.toNavigatorTranslatableText(),
            thumbnailUrl = thumbnailUrl,
            colorPalette = colorPalette.toNavigatorColorPalette()
        )
    }

    sealed class Event {
        data class OpenCollectionDetails(val collection: Navigator.Collection) : Event()
        object ShowErrorMessage : Event()
    }
}