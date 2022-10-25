package com.chignonMignon.wallpapers.presentation.feature

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

interface Navigator {

    fun navigateToCollections()

    fun navigateToCollectionDetails(collection: Collection)

    fun navigateToWallpaperDetails(wallpaperId: String)

    fun navigateToAbout()

    fun navigateBack()

    @Parcelize
    data class Collection internal constructor(
        val id: String,
        val name: String,
        val description: String,
        val thumbnailUrl: String,
        val backgroundColor: Int,
        val foregroundColor: Int
    ) : Parcelable
}