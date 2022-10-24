package com.chignonMignon.wallpapers.presentation.feature

interface Navigator {

    fun navigateToCollections()

    fun navigateToCollectionDetails(collectionId: String)

    fun navigateToWallpaperDetails(wallpaperId: String)

    fun navigateToAbout()
}