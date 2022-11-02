package com.chignonMignon.wallpapers.presentation.debugMenu

import android.app.Application
import androidx.annotation.StyleRes
import com.chignonMignon.wallpapers.data.model.Result
import com.chignonMignon.wallpapers.data.model.domain.Collection
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

    suspend fun getMockCollections(): Result<List<Collection>>? = null

    suspend fun getMockWallpapers(
        collectionId: String
    ): Result<List<Wallpaper>>? = null
}