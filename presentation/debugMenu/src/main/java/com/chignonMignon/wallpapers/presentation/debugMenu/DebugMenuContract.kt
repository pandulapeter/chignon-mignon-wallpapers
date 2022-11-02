package com.chignonMignon.wallpapers.presentation.debugMenu

import android.app.Application
import androidx.annotation.StyleRes

interface DebugMenuContract {

    fun initialize(
        application: Application,
        applicationTitle: String,
        versionName: String,
        versionCode: Int,
        @StyleRes themeResourceId: Int
    ) = Unit
}