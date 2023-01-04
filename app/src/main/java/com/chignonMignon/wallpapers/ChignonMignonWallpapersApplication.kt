package com.chignonMignon.wallpapers

import android.app.Application
import com.chignonMignon.wallpapers.data.repository.repositoryModule
import com.chignonMignon.wallpapers.data.source.localImpl.localSourceModule
import com.chignonMignon.wallpapers.data.source.remote.remoteSourceModule
import com.chignonMignon.wallpapers.domain.domainModule
import com.chignonMignon.wallpapers.presentation.about.presentationAboutModule
import com.chignonMignon.wallpapers.presentation.collectionDetails.presentationCollectionDetailsModule
import com.chignonMignon.wallpapers.presentation.collections.presentationCollectionsModule
import com.chignonMignon.wallpapers.presentation.debugMenu.DebugMenu
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.presentationWallpaperDetailsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ChignonMignonWallpapersApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ChignonMignonWallpapersApplication)
            modules(
                localSourceModule +
                        remoteSourceModule +
                        repositoryModule +
                        domainModule +
                        presentationAboutModule +
                        presentationCollectionsModule +
                        presentationCollectionDetailsModule +
                        presentationWallpaperDetailsModule
            )
        }
        DebugMenu.initialize(
            application = this,
            applicationTitle = getString(R.string.chignon_mignon_wallpapers),
            versionName = BuildConfig.VERSION_NAME,
            versionCode = BuildConfig.VERSION_CODE,
            themeResourceId = R.style.ChignonMignon
        )
    }
}