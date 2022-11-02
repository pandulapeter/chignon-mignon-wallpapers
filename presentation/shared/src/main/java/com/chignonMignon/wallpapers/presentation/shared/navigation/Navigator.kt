package com.chignonMignon.wallpapers.presentation.shared.navigation

import android.view.View
import com.chignonMignon.wallpapers.presentation.shared.navigation.model.CollectionDestination
import com.chignonMignon.wallpapers.presentation.shared.navigation.model.WallpaperDestination

interface Navigator {

    fun navigateToCollections()

    fun navigateToCollectionDetails(
        collectionDestination: CollectionDestination,
        sharedElements: List<View>
    )

    fun navigateToWallpaperDetails(
        wallpaperDestination: WallpaperDestination,
        sharedElements: List<View>
    )

    fun navigateBack()
}