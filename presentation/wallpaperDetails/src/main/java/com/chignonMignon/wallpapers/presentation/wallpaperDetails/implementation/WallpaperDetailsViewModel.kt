package com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation

import android.content.Context
import android.graphics.Rect
import androidx.annotation.ColorInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chignonMignon.wallpapers.data.model.Result
import com.chignonMignon.wallpapers.data.model.domain.Product
import com.chignonMignon.wallpapers.domain.useCases.GetProductsByWallpaperIdUseCase
import com.chignonMignon.wallpapers.presentation.debugMenu.DebugMenu
import com.chignonMignon.wallpapers.presentation.shared.navigation.model.WallpaperDestination
import com.chignonMignon.wallpapers.presentation.utilities.eventFlow
import com.chignonMignon.wallpapers.presentation.utilities.extensions.downloadImage
import com.chignonMignon.wallpapers.presentation.utilities.extensions.pushEvent
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.productList.WallpaperDetailsProductListItem
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.wallpaperList.WallpaperDetailsListItem
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.wallpaperList.WallpaperType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
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
    private val _pagerProgress = MutableStateFlow(getPagerProgress(initialWallpaperIndex))
    val pagerProgress: StateFlow<Float> = _pagerProgress
    val productListItems = focusedWallpaper.map { wallpaper ->
        (getProductsByWallpaperId(false, wallpaper.id)).let { result ->
            val products = (result as? Result.Success)?.data
            if (products.isNullOrEmpty()) {
                listOf(WallpaperDetailsProductListItem.EmptyUiModel())
            } else {
                products.map { WallpaperDetailsProductListItem.ProductUiModel(it) }
            }
        }
    }

    init {
        DebugMenu.log("Opened wallpaper details.")
    }

    fun onPageSelected(position: Int) {
        _focusedWallpaper.value = wallpapers[position]
        _pagerProgress.value = getPagerProgress(position)
    }

    fun onSetWallpaperButtonPressed(context: Context, wallpaper: WallpaperDestination, cropRect: Rect, wallpaperType: WallpaperType) = viewModelScope.launch(Dispatchers.IO) {
        if (!_shouldShowLoadingIndicator.value) {
            _shouldShowLoadingIndicator.value = true
            DebugMenu.log("Downloading wallpaper: ${wallpaper.id}...")
            context.downloadImage(wallpaper.url).let { bitmap ->
                if (bitmap == null) {
                    DebugMenu.log("Error while downloading wallpaper.")
                    _events.pushEvent(Event.ShowErrorMessage(wallpaper, cropRect, wallpaperType))
                } else {
                    DebugMenu.log("Setting wallpaper with crop region ${cropRect.left}, ${cropRect.top}, ${cropRect.width()}, ${cropRect.height()}...")
                    val result = context.setWallpaperBitmap(bitmap, cropRect, wallpaperType)
                    if (result) {
                        DebugMenu.log("Wallpaper set.")
                        _events.pushEvent(Event.WallpaperSet)
                    } else {
                        DebugMenu.log("Wallpaper not set.")
                        _events.pushEvent(Event.WallpaperNotSet)
                    }
                }
            }

            _shouldShowLoadingIndicator.value = false
        }
    }

    fun updatePrimaryColor(@ColorInt primaryColor: Int) {
        _primaryColor.value = primaryColor
    }

    fun onProductSelected(product: Product) = _events.pushEvent(Event.OpenUrl(product.url))

    private fun getPagerProgress(position: Int) = if (wallpapers.isNotEmpty()) (position + 1f) / wallpapers.size.toFloat() else 0f

    sealed class Event {
        object WallpaperSet : Event()
        object WallpaperNotSet : Event()
        data class ShowErrorMessage(val wallpaper: WallpaperDestination, val cropRect: Rect, val wallpaperType: WallpaperType) : Event()
        data class OpenUrl(val url: String) : Event()
    }
}