package com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation

import android.content.Context
import android.net.Uri
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
import kotlinx.coroutines.delay
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
        (DebugMenu.getMockProducts(wallpaper.id, false) ?: getProductsByWallpaperId(false, wallpaper.id)).let { result ->
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

    fun onSetWallpaperButtonPressed(context: Context, wallpaper: WallpaperDestination) = viewModelScope.launch {
        if (!_shouldShowLoadingIndicator.value) {
            _shouldShowLoadingIndicator.value = true
            DebugMenu.log("Downloading wallpaper: ${wallpaper.id}...")
            delay(1000)
            _events.pushEvent(
                context.downloadImage(wallpaper.url).let { bitmap ->
                    if (bitmap == null) {
                        DebugMenu.log("Error while downloading wallpaper.")
                        Event.ShowErrorMessage(wallpaper)
                    } else {
                        DebugMenu.log("Setting wallpaper...")
                        if (context.setWallpaperBitmap(bitmap)) {
                            DebugMenu.log("Wallpaper set.")
                            Event.WallpaperSet
                        } else {
                            DebugMenu.log("Wallpaper not set.")
                            Event.WallpaperNotSet
                        }
//                        context.saveImage(context.getWallpaperFile(wallpaper.id), bitmap).let { uri ->
//                            if (uri == null) {
//                                DebugMenu.log("Error while saving wallpaper.")
//                                Event.ShowErrorMessage(wallpaper)
//                            } else {
//                                DebugMenu.log("Setting wallpaper...")
//                                Event.SetWallpaper(uri)
//                            }
//                        }
                    }
                }
            )
            _shouldShowLoadingIndicator.value = false
        }
    }

    fun updatePrimaryColor(@ColorInt primaryColor: Int) {
        _primaryColor.value = primaryColor
    }

    fun onProductSelected(product: Product) = _events.pushEvent(Event.OpenUrl(product.url))

    private fun getPagerProgress(position: Int) = if (wallpapers.isNotEmpty()) (position + 1f) / wallpapers.size.toFloat() else 0f

    sealed class Event {
        data class SetWallpaper(val uri: Uri) : Event()
        object WallpaperSet : Event()
        object WallpaperNotSet : Event()
        data class ShowErrorMessage(val wallpaper: WallpaperDestination) : Event()
        data class OpenUrl(val url: String) : Event()
    }
}