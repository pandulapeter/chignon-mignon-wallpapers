package com.chignonMignon.wallpapers.presentation.debugMenu

import android.app.Application
import android.util.Log
import androidx.annotation.StyleRes
import com.chignonMignon.wallpapers.presentation.debugMenu.sections.createDataSection
import com.chignonMignon.wallpapers.presentation.debugMenu.sections.createGeneralSection
import com.chignonMignon.wallpapers.presentation.debugMenu.sections.createHeaderSection
import com.chignonMignon.wallpapers.presentation.debugMenu.sections.createLogsSection
import com.chignonMignon.wallpapers.presentation.debugMenu.sections.createTestingSection
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
            ) + createDataSection(
            ) + createLogsSection(
            )).toTypedArray()
        )
        log("Debug menu initialized")
    }

    override fun log(text: String) {
        Log.d("ChignonMignonLogs", text)
        Beagle.log(text)
    }
}