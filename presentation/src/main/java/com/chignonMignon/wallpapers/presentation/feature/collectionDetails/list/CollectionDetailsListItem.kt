package com.chignonMignon.wallpapers.presentation.feature.collectionDetails.list

import com.chignonMignon.wallpapers.presentation.feature.Navigator
import com.chignonMignon.wallpapers.presentation.utilities.list.ListItem

internal sealed class CollectionDetailsListItem(
    override val id: String
) : ListItem {

    data class WallpaperUiModel(
        val wallpaper: Navigator.Wallpaper
    ) : CollectionDetailsListItem("wallpaper_${wallpaper.id}")
}