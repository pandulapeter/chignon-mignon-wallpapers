package com.chignonMignon.wallpapers.presentation.collections.implementation

import android.content.Context
import android.view.View
import androidx.annotation.ColorInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chignonMignon.wallpapers.data.model.Result
import com.chignonMignon.wallpapers.data.model.domain.Collection
import com.chignonMignon.wallpapers.domain.useCases.AreCollectionsAvailableUseCase
import com.chignonMignon.wallpapers.domain.useCases.GetCollectionsUseCase
import com.chignonMignon.wallpapers.domain.useCases.GetProductsUseCase
import com.chignonMignon.wallpapers.domain.useCases.GetWallpapersUseCase
import com.chignonMignon.wallpapers.presentation.collections.BuildConfig
import com.chignonMignon.wallpapers.presentation.collections.implementation.list.CollectionsListItem
import com.chignonMignon.wallpapers.presentation.debugMenu.DebugMenu
import com.chignonMignon.wallpapers.presentation.shared.R
import com.chignonMignon.wallpapers.presentation.shared.colorPaletteGenerator.ColorPaletteGenerator
import com.chignonMignon.wallpapers.presentation.shared.extensions.toNavigatorColorPalette
import com.chignonMignon.wallpapers.presentation.shared.extensions.toNavigatorTranslatableText
import com.chignonMignon.wallpapers.presentation.shared.navigation.model.CollectionDestination
import com.chignonMignon.wallpapers.presentation.shared.navigation.model.ColorPaletteModel
import com.chignonMignon.wallpapers.presentation.utilities.eventFlow
import com.chignonMignon.wallpapers.presentation.utilities.extensions.color
import com.chignonMignon.wallpapers.presentation.utilities.extensions.colorResource
import com.chignonMignon.wallpapers.presentation.utilities.extensions.pushEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
    private val getProducts: GetProductsUseCase,
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
    private val _isLastPageFocused = MutableStateFlow(false)
    val isLastPageFocused: StateFlow<Boolean> = _isLastPageFocused
    private val _focusedCollectionDestination = MutableStateFlow<CollectionDestination?>(null)
    val focusedCollectionDestination: StateFlow<CollectionDestination?> = _focusedCollectionDestination
    val areCollectionsLoaded = collections.map { it != null }.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val shouldShowAboutButton = collections.map { it?.isNotEmpty() == true }.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val selectedTitle = combine(
        collections,
        focusedCollectionDestination,
        _isLastPageFocused
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
    private val primaryColor = MutableStateFlow<Int?>(null)
    private val secondaryColor = MutableStateFlow<Int?>(null)
    private val _onSecondaryColor = MutableStateFlow<Int?>(null)
    val onSecondaryColor: StateFlow<Int?> = _onSecondaryColor
    val backgroundColor = combine(primaryColor, secondaryColor) { primaryColor, secondaryColor -> primaryColor to secondaryColor }
    private var colorPaletteGeneratorJob: Job? = null

    fun loadData(isForceRefresh: Boolean, context: Context) = viewModelScope.launch {
        if (!_shouldShowLoadingIndicator.value) {
            val areCollectionsAvailable = areCollectionsAvailable()
            _shouldShowLoadingIndicator.value = isForceRefresh || !areCollectionsAvailable
            if (!areCollectionsAvailable) {
                _events.pushEvent(Event.ScrollToWelcome)
            }
            val loadResult = listOf(
                async { loadCollections(isForceRefresh) },
                async { loadProducts(isForceRefresh) },
                async { loadWallpapers(isForceRefresh) }
            ).awaitAll()
            @Suppress("UNCHECKED_CAST")
            val collectionsData = loadResult.firstOrNull { it.second != null }?.second as? List<Collection>
            if (loadResult.all { it.first } && collectionsData != null) {
                colorPaletteGeneratorJob?.cancel()
                val filteredResults = collectionsData.filter { BuildConfig.DEBUG || it.isPublic }
                if (collections.value?.map { it.thumbnailUrl } != filteredResults.map { it.thumbnailUrl }) {
                    collections.value = filteredResults.map { it.toPlaceholderNavigatorCollection(context) }
                }
                colorPaletteGeneratorJob = launch {
                    filteredResults.map { collection ->
                        async {
                            collection.toNavigatorCollection().let { collectionWithFinalColorPalette ->
                                if (collections.value?.contains(collectionWithFinalColorPalette) == false) {
                                    collections.value = collections.value?.toMutableList()?.apply {
                                        val index = indexOfFirst { it.id == collectionWithFinalColorPalette.id }
                                        if (index != -1) {
                                            removeAt(index)
                                            add(index, collectionWithFinalColorPalette)
                                        }
                                    }
                                }
                            }
                        }
                    }.awaitAll()
                }
                _shouldShowLoadingIndicator.value = false
            } else {
                _events.pushEvent(Event.ShowErrorMessage)
                _shouldShowLoadingIndicator.value = false
            }
        }
    }

    fun onPageSelected(position: Int) {
        collections.value?.let { collections ->
            if (collections.isNotEmpty()) {
                _pagerProgress.value = (position.toFloat()) / collections.size.toFloat()
            }
            _isLastPageFocused.value = position == collections.size + 1
            _focusedCollectionDestination.value = if (position >= 1 && position <= collections.size) collections[position - 1] else null
        }
    }

    fun updatePrimaryColor(@ColorInt primaryColor: Int) {
        this.primaryColor.value = primaryColor
    }

    fun updateSecondaryColor(@ColorInt secondaryColor: Int) {
        this.secondaryColor.value = secondaryColor
    }

    fun updateOnSecondaryColor(@ColorInt onSecondaryColor: Int) {
        _onSecondaryColor.value = onSecondaryColor
    }

    fun onItemSelected(collectionId: String, sharedElements: List<View>) = collections.value?.firstOrNull { it.id == collectionId }?.let {
        if (it.thumbnailUrl.isNotBlank()) {
            _events.pushEvent(Event.OpenCollectionDetails(it, sharedElements))
        }
    }

    fun onAboutButtonPressed() {
        if (areCollectionsLoaded.value) {
            _events.pushEvent(Event.NavigateToAboutPage)
        }
    }

    fun onPreviousButtonPressed() {
        if (focusedCollectionDestination.value != null || _isLastPageFocused.value) {
            _events.pushEvent(Event.NavigateToPreviousPage)
        }
    }

    fun onNextButtonPressed() {
        if (collections.value != null && (focusedCollectionDestination.value != null || !_isLastPageFocused.value)) {
            _events.pushEvent(Event.NavigateToNextPage)
        }
    }

    private suspend fun loadCollections(isForceRefresh: Boolean): Pair<Boolean, List<Collection>?> {
        DebugMenu.log("Loading collections...")
        return when (val result = DebugMenu.getMockCollections(isForceRefresh) ?: getCollections(isForceRefresh)) {
            is Result.Success -> true.also { DebugMenu.log("Loaded ${result.data.size} collections.") } to result.data
            is Result.Failure -> false.also { DebugMenu.log("Failed to load collections: ${result.exception.message}.") } to null
        }
    }

    private suspend fun loadProducts(isForceRefresh: Boolean): Pair<Boolean, Any?> {
        DebugMenu.log("Loading products...")
        return when (val result = DebugMenu.getMockProducts("", isForceRefresh) ?: getProducts(isForceRefresh)) {
            is Result.Success -> true.also { DebugMenu.log("Loaded ${result.data.size} products.") }
            is Result.Failure -> false.also { DebugMenu.log("Failed to load products: ${result.exception.message}.") }
        } to null
    }

    private suspend fun loadWallpapers(isForceRefresh: Boolean): Pair<Boolean, Any?> {
        DebugMenu.log("Loading wallpapers...")
        return when (val result = DebugMenu.getMockWallpapers("", isForceRefresh) ?: getWallpapers(isForceRefresh)) {
            is Result.Success -> true.also { DebugMenu.log("Loaded ${result.data.size} wallpapers.") }
            is Result.Failure -> false.also { DebugMenu.log("Failed to load wallpapers: ${result.exception.message}.") }
        } to null
    }

    private fun Collection.toPlaceholderNavigatorCollection(context: Context) = CollectionDestination(
        id = id,
        name = name.toNavigatorTranslatableText(),
        description = description.toNavigatorTranslatableText(),
        thumbnailUrl = "",
        colorPaletteModel = ColorPaletteModel(
            primary = context.colorResource(android.R.attr.windowBackground),
            secondary = context.color(R.color.primary),
            onSecondary = context.color(R.color.on_primary)
        ),
        isPublic = isPublic
    )

    private suspend fun Collection.toNavigatorCollection() = colorPaletteGenerator.generateColors(
        imageUrl = thumbnailUrl,
        overridePrimaryColorCode = primaryColorCode,
        overrideSecondaryColorCode = secondaryColorCode,
        overrideOnSecondaryColorCode = onSecondaryColorCode
    ).let { colorPalette ->
        CollectionDestination(
            id = id,
            name = name.toNavigatorTranslatableText(),
            description = description.toNavigatorTranslatableText(),
            thumbnailUrl = thumbnailUrl,
            colorPaletteModel = colorPalette.toNavigatorColorPalette(),
            isPublic = isPublic
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