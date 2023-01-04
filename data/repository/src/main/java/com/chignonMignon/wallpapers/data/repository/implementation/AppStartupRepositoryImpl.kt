package com.chignonMignon.wallpapers.data.repository.implementation

import com.chignonMignon.wallpapers.data.repository.api.AppStartupRepository

internal class AppStartupRepositoryImpl(
) : AppStartupRepository {

    private var appStartup = true

    override fun isAppStartup() = appStartup.also {
        appStartup = false
    }
}