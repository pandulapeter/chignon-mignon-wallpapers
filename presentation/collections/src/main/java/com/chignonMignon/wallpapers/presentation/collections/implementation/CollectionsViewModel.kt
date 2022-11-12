package com.chignonMignon.wallpapers.presentation.collections.implementation

import android.view.View
import androidx.annotation.ColorInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chignonMignon.wallpapers.data.model.Result
import com.chignonMignon.wallpapers.data.model.domain.Collection
import com.chignonMignon.wallpapers.domain.useCases.AreCollectionsAvailableUseCase
import com.chignonMignon.wallpapers.domain.useCases.GetCollectionsUseCase
import com.chignonMignon.wallpapers.domain.useCases.GetWallpapersUseCase
import com.chignonMignon.wallpapers.presentation.collections.implementation.list.CollectionsListItem
import com.chignonMignon.wallpapers.presentation.debugMenu.DebugMenu
import com.chignonMignon.wallpapers.presentation.shared.R
import com.chignonMignon.wallpapers.presentation.shared.colorPaletteGenerator.ColorPaletteGenerator
import com.chignonMignon.wallpapers.presentation.shared.extensions.toNavigatorColorPalette
import com.chignonMignon.wallpapers.presentation.shared.extensions.toNavigatorTranslatableText
import com.chignonMignon.wallpapers.presentation.shared.navigation.model.CollectionDestination
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

internal class CollectionsViewModel(
    private val areCollectionsAvailable: AreCollectionsAvailableUseCase,
    private val getCollections: GetCollectionsUseCase,
    private val getWallpapers: GetWallpapersUseCase,
    private val colorPaletteGenerator: ColorPaletteGenerator
) : ViewModel() {
    private val _events = eventFlow<Event>()
    val events: Flow<Event> = _events
    private val collections = MutableStateFlow<List<CollectionDestination>?>(null)
    private val _shouldShowLoadingIndicator = MutableStateFlow(false)
    val shouldShowLoadingIndicator: StateFlow<Boolean> = _shouldShowLoadingIndicator
    val isViewPagerSwipeEnabled = combine(
        shouldShowLoadingIndicator,
        collections
    ) { shouldShowLoadingIndicator,
        collections ->
        collections != null || !shouldShowLoadingIndicator
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val items = collections.map { collections ->
        buildList {
            if (!areCollectionsAvailable() || collections != null) {
                add(CollectionsListItem.WelcomeUiModel())
                when {
                    collections == null -> add(CollectionsListItem.ErrorUiModel())
                    collections.isEmpty() -> add(CollectionsListItem.EmptyUiModel())
                    else -> addAll(collections.map { CollectionsListItem.CollectionUiModel(it) } + CollectionsListItem.AboutUiModel())
                }
            }
        }
    }
    private val isLastPageFocused = MutableStateFlow(false)
    private val _focusedCollectionDestination = MutableStateFlow<CollectionDestination?>(null)
    val focusedCollectionDestination: StateFlow<CollectionDestination?> = _focusedCollectionDestination
    val areCollectionsLoaded = collections.map { it != null }.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val shouldShowAboutButton = collections.map { it?.isNotEmpty() == true }.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val selectedTitle = combine(
        collections,
        focusedCollectionDestination,
        isLastPageFocused
    ) { collections,
        focusedCollection,
        isLastPageFocused ->
        if (focusedCollection == null && (!collections.isNullOrEmpty() || !isLastPageFocused)) if (isLastPageFocused) R.string.about_title else R.string.collections_welcome else R.string.collections_title
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)
    private val _pagerProgress = MutableStateFlow(0f)
    val pagerProgress: StateFlow<Float> = _pagerProgress
    val shouldShowProgressBar = combine(shouldShowAboutButton, pagerProgress) { shouldShowAboutButton, pagerProgress ->
        shouldShowAboutButton && pagerProgress <= 1f
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    private val _primaryColor = MutableStateFlow<Int?>(null)
    val primaryColor: StateFlow<Int?> = _primaryColor
    private val _secondaryColor = MutableStateFlow<Int?>(null)
    val secondaryColor: StateFlow<Int?> = _secondaryColor
    private val _onSecondaryColor = MutableStateFlow<Int?>(null)
    val onSecondaryColor: StateFlow<Int?> = _onSecondaryColor

    init {
        loadData(false)
        viewModelScope.launch {
            DebugMenu.log("Loading wallpapers (pre-fetch)...")
            when (val result = DebugMenu.getMockWallpapers("", false) ?: getWallpapers(false)) {
                is Result.Success -> DebugMenu.log("Loaded ${result.data.size} wallpapers.")
                is Result.Failure -> DebugMenu.log("Failed to load wallpapers: ${result.exception.message}.")
            }
        }
    }

    fun loadData(isForceRefresh: Boolean) = viewModelScope.launch {
        if (!_shouldShowLoadingIndicator.value) {
            _shouldShowLoadingIndicator.value = true
            if (!areCollectionsAvailable()) {
                _events.pushEvent(Event.ScrollToWelcome)
            }
            DebugMenu.log("Loading collections (force refresh: $isForceRefresh)...")
            when (val result = DebugMenu.getMockCollections(isForceRefresh) ?: getCollections(isForceRefresh)) {
                is Result.Success -> {
                    DebugMenu.log("Loaded ${result.data.size} collections.")
                    collections.value = result.data.map { it.toNavigatorCollection() }
                    _shouldShowLoadingIndicator.value = false
                }
                is Result.Failure -> {
                    DebugMenu.log("Failed to load collections: ${result.exception.message}.")
                    _events.pushEvent(Event.ShowErrorMessage)
                    _shouldShowLoadingIndicator.value = false
                }
            }
        }
    }

    fun onPageSelected(position: Int) {
        collections.value?.let { collections ->
            if (collections.isNotEmpty()) {
                _pagerProgress.value = (position.toFloat()) / collections.size.toFloat()
            }
            isLastPageFocused.value = position == collections.size + 1
            _focusedCollectionDestination.value = if (position >= 1 && position <= collections.size) collections[position - 1] else null
        }
    }

    fun updatePrimaryColor(@ColorInt primaryColor: Int) {
        _primaryColor.value = primaryColor
    }

    fun updateSecondaryColor(@ColorInt secondaryColor: Int) {
        _secondaryColor.value = secondaryColor
    }

    fun updateOnSecondaryColor(@ColorInt onSecondaryColor: Int) {
        _onSecondaryColor.value = onSecondaryColor
    }

    fun onItemSelected(collectionId: String, sharedElements: List<View>) = collections.value?.firstOrNull { it.id == collectionId }?.let {
        _events.pushEvent(Event.OpenCollectionDetails(it, sharedElements))
    }

    fun onAboutButtonPressed() {
        if (areCollectionsLoaded.value) {
            _events.pushEvent(Event.NavigateToAboutPage)
        }
    }

    fun onPreviousButtonPressed() {
        if (focusedCollectionDestination.value != null || isLastPageFocused.value) {
            _events.pushEvent(Event.NavigateToPreviousPage)
        }
    }

    fun onNextButtonPressed() {
        if (collections.value != null && (focusedCollectionDestination.value != null || !isLastPageFocused.value)) {
            _events.pushEvent(Event.NavigateToNextPage)
        }
    }

    private suspend fun Collection.toNavigatorCollection() = colorPaletteGenerator.generateColors(thumbnailUrl).let { colorPalette ->
        CollectionDestination(
            id = id,
            name = name.toNavigatorTranslatableText(),
            description = description.toNavigatorTranslatableText(),
            thumbnailUrl = thumbnailUrl,
            colorPaletteModel = colorPalette.toNavigatorColorPalette()
        )
    }

    sealed class Event {
        data class OpenCollectionDetails(val collectionDestination: CollectionDestination, val sharedElements: List<View>) : Event()
        object NavigateToPreviousPage : Event()
        object NavigateToNextPage : Event()
        object NavigateToAboutPage : Event()
        object ShowErrorMessage : Event()
        object ScrollToWelcome : Event()
    }
}