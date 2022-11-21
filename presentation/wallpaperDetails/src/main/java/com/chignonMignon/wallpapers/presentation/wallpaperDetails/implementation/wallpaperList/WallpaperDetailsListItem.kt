package com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.wallpaperList

import com.chignonMignon.wallpapers.presentation.shared.navigation.model.WallpaperDestination
import com.chignonMignon.wallpapers.presentation.utilities.list.ListItem

internal sealed class WallpaperDetailsListItem(override val id: String) : ListItem {

    data class WallpaperUiModel(
        val wallpaper: WallpaperDestination
    ) : WallpaperDetailsListItem("wallpaper_${wallpaper.id}")
}