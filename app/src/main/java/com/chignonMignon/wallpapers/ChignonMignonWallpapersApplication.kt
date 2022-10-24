package com.chignonMignon.wallpapers

import android.app.Application
import com.chignonMignon.wallpapers.data.repository.repositoryModule
import com.chignonMignon.wallpapers.data.source.remote.sourceModule
import com.chignonMignon.wallpapers.domain.domainModule
import com.chignonMignon.wallpapers.presentation.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ChignonMignonWallpapersApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ChignonMignonWallpapersApplication)
            modules(sourceModule + repositoryModule + domainModule + presentationModule)
        }
    }
}