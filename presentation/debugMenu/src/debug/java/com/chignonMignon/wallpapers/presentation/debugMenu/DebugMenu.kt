package com.chignonMignon.wallpapers.presentation.debugMenu

import android.app.Application
import android.util.Log
import androidx.annotation.StyleRes
import com.chignonMignon.wallpapers.data.model.Result
import com.chignonMignon.wallpapers.presentation.debugMenu.sections.createGeneralSection
import com.chignonMignon.wallpapers.presentation.debugMenu.sections.createHeaderSection
import com.chignonMignon.wallpapers.presentation.debugMenu.sections.createLogsSection
import com.chignonMignon.wallpapers.presentation.debugMenu.sections.createTestingSection
import com.chignonMignon.wallpapers.presentation.debugMenu.sections.generateMockCollections
import com.chignonMignon.wallpapers.presentation.debugMenu.sections.generateMockWallpapers
import com.chignonMignon.wallpapers.presentation.debugMenu.sections.shouldUseMockData
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.common.configuration.Appearance
import com.pandulapeter.beagle.common.configuration.Behavior
import com.pandulapeter.beagle.logCrash.BeagleCrashLogger

object DebugMenu : DebugMenuContract {

    override fun initialize(
        application: Application,
        applicationTitle: String,
        versionName: String,
        versionCode: Int,
        @StyleRes themeResourceId: Int
    ) {
        Beagle.initialize(
            application = application,
            appearance = Appearance(
                themeResourceId = themeResourceId,
                applyInsets = insetHandler
            ),
            behavior = Behavior(
                bugReportingBehavior = Behavior.BugReportingBehavior(
                    crashLoggers = listOf(BeagleCrashLogger)
                )
            )
        )
        Beagle.set(
            modules = (createHeaderSection(
                applicationTitle = applicationTitle,
                versionName = versionName,
                versionCode = versionCode
            ) + createGeneralSection(
            ) + createTestingSection(
            ) + createLogsSection(
            )).toTypedArray()
        )
        log("Debug menu initialized")
    }

    override fun log(text: String) {
        Log.d("ChignonMignonLogs", text)
        Beagle.log(text)
    }

    override suspend fun getMockCollections() = if (shouldUseMockData) Result.Success(generateMockCollections()) else null

    override suspend fun getMockWallpapers(collectionId: String) = if (shouldUseMockData) Result.Success(generateMockWallpapers(collectionId)) else null
}