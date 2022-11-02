package com.chignonMignon.wallpapers.presentation.collectionDetails.implementation.list

import com.chignonMignon.wallpapers.presentation.shared.navigation.model.WallpaperDestination
import com.chignonMignon.wallpapers.presentation.utilities.list.ListItem

internal sealed class CollectionDetailsListItem(
    override val id: String
) : ListItem {

    data class WallpaperUiModel(
        val wallpaperDestination: WallpaperDestination
    ) : CollectionDetailsListItem("wallpaper_${wallpaperDestination.id}")
}