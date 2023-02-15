package com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.ColorInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chignonMignon.wallpapers.data.model.Result
import com.chignonMignon.wallpapers.data.model.domain.Product
import com.chignonMignon.wallpapers.domain.useCases.GetProductsByWallpaperIdUseCase
import com.chignonMignon.wallpapers.presentation.debugMenu.DebugMenu
import com.chignonMignon.wallpapers.presentation.shared.navigation.model.WallpaperDestination
import com.chignonMignon.wallpapers.presentation.utilities.eventFlow
import com.chignonMignon.wallpapers.presentation.utilities.extensions.pushEvent
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.productList.WallpaperDetailsProductListItem
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.wallpaperList.WallpaperDetailsListItem
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.wallpaperList.WallpaperType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class WallpaperDetailsViewModel(
    private val wallpapers: List<WallpaperDestination>,
    initialWallpaperIndex: Int,
    private val getProductsByWallpaperId: GetProductsByWallpaperIdUseCase
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
    private val _secondaryColor = MutableStateFlow<Int?>(null)
    val secondaryColor: StateFlow<Int?> = _secondaryColor
    private val _pagerProgress = MutableStateFlow(getPagerProgress(initialWallpaperIndex))
    val pagerProgress: StateFlow<Float> = _pagerProgress
    val productListItems = focusedWallpaper.map { wallpaper ->
        (getProductsByWallpaperId(false, wallpaper.id)).let { result ->
            (result as? Result.Success)?.data?.map { WallpaperDetailsProductListItem.ProductUiModel(it) }.orEmpty()
        }
    }
    val shouldShowGradient = productListItems.map { it.isNotEmpty() }.stateIn(viewModelScope, SharingStarted.Eagerly, true)

    init {
        DebugMenu.log("Opened wallpaper details.")
    }

    fun onPageSelected(position: Int) {
        _focusedWallpaper.value = wallpapers[position]
        _pagerProgress.value = getPagerProgress(position)
    }

    fun onSetWallpaperButtonPressed(context: Context, bitmap: Bitmap, wallpaperType: WallpaperType) =
        viewModelScope.launch(Dispatchers.IO) {
            if (!_shouldShowLoadingIndicator.value) {
                _shouldShowLoadingIndicator.value = true
                val result = context.setWallpaperBitmap(bitmap, wallpaperType)
                if (result) {
                    DebugMenu.log("Wallpaper set.")
                    _events.pushEvent(Event.WallpaperSet)
                } else {
                    DebugMenu.log("Wallpaper not set.")
                    _events.pushEvent(Event.WallpaperNotSet)
                }
                _shouldShowLoadingIndicator.value = false
            }
        }

    fun updatePrimaryColor(@ColorInt primaryColor: Int) {
        _primaryColor.value = primaryColor
    }

    fun updateSecondaryColor(@ColorInt secondaryColor: Int) {
        _secondaryColor.value = secondaryColor
    }

    fun onProductSelected(product: Product) = _events.pushEvent(Event.OpenUrl(product.url))

    private fun getPagerProgress(position: Int) = if (wallpapers.isNotEmpty()) (position + 1f) / wallpapers.size.toFloat() else 0f

    sealed class Event {
        object WallpaperSet : Event()
        object WallpaperNotSet : Event()
        data class OpenUrl(val url: String) : Event()
    }
}