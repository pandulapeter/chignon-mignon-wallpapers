package com.chignonMignon.wallpapers.presentation.debugMenu

import android.app.Application
import androidx.annotation.StyleRes
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
                themeResourceId = themeResourceId
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
            ) + createTestingSection()).toTypedArray()
        )
    }
}