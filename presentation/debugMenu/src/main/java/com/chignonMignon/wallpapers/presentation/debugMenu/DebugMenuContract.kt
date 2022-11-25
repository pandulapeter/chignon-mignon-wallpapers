package com.chignonMignon.wallpapers.presentation.debugMenu

import android.app.Application
import androidx.annotation.StyleRes
import com.chignonMignon.wallpapers.data.model.Result
import com.chignonMignon.wallpapers.data.model.domain.Collection
import com.chignonMignon.wallpapers.data.model.domain.Product
import com.chignonMignon.wallpapers.data.model.domain.Wallpaper

interface DebugMenuContract {

    fun initialize(
        application: Application,
        applicationTitle: String,
        versionName: String,
        versionCode: Int,
        @StyleRes themeResourceId: Int
    ) = Unit

    fun log(
        text: String
    ) = Unit

    suspend fun getMockCollections(
        isForceRefresh: Boolean
    ): Result<List<Collection>>? = null

    suspend fun getMockProducts(
        wallpaperId: String,
        isForceRefresh: Boolean
    ): Result<List<Product>>? = null

    suspend fun getMockWallpapers(
        collectionId: String,
        isForceRefresh: Boolean
    ): Result<List<Wallpaper>>? = null
}