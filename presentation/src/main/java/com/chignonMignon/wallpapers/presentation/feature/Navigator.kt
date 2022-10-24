package com.chignonMignon.wallpapers.presentation.feature

interface Navigator {

    fun navigateToCategories()

    fun navigateToCategoryDetails(categoryId: String)

    fun navigateToWallpaperDetails(wallpaperId: String)

    fun navigateToAbout()
}