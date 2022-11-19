package com.chignonMignon.wallpapers

import android.animation.Animator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.chignonMignon.wallpapers.presentation.about.AboutFragment
import com.chignonMignon.wallpapers.presentation.collectionDetails.CollectionDetailsFragment
import com.chignonMignon.wallpapers.presentation.collections.CollectionsFragment
import com.chignonMignon.wallpapers.presentation.shared.navigation.Navigator
import com.chignonMignon.wallpapers.presentation.shared.navigation.model.CollectionDestination
import com.chignonMignon.wallpapers.presentation.shared.navigation.model.WallpaperDestination
import com.chignonMignon.wallpapers.presentation.utilities.extensions.handleReplace
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.WallpaperDetailsFragment

class ChignonMignonWallpapersActivity : AppCompatActivity(R.layout.activity_chignon_mignon_wallpapers), Navigator {

    override var selectedWallpaperIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        handleSplashScreen()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        if (savedInstanceState == null) {
            navigateToCollections()
        }
    }

    override fun navigateToAbout() = supportFragmentManager.handleReplace(
        containerId = R.id.container,
        addToBackStack = true
    ) {
        AboutFragment.newInstance()
    }

    override fun navigateToCollections() = supportFragmentManager.handleReplace(
        containerId = R.id.container
    ) {
        CollectionsFragment.newInstance()
    }

    override fun navigateToCollectionDetails(
        collectionDestination: CollectionDestination,
        sharedElements: List<View>
    ) {
        selectedWallpaperIndex = 0
        supportFragmentManager.handleReplace(
            containerId = R.id.container,
            sharedElements = sharedElements,
            addToBackStack = true
        ) {
            CollectionDetailsFragment.newInstance(collectionDestination)
        }
    }

    override fun navigateToWallpaperDetails(
        wallpapers: List<WallpaperDestination>,
        selectedWallpaperIndex: Int,
        sharedElements: List<View>
    ) = supportFragmentManager.handleReplace(
        containerId = R.id.container,
        sharedElements = sharedElements,
        addToBackStack = true
    ) {
        WallpaperDetailsFragment.newInstance(
            wallpapers = wallpapers,
            selectedWallpaperIndex = selectedWallpaperIndex
        )
    }

    override fun navigateBack() = supportFragmentManager.popBackStack()

    private fun handleSplashScreen() =
        installSplashScreen().setOnExitAnimationListener { splashScreen ->
            splashScreen.view.animate()
                .scaleX(2f)
                .scaleY(2f)
                .alpha(0f)
                .setInterpolator(AccelerateInterpolator())
                .setListener(
                    object : Animator.AnimatorListener {
                        override fun onAnimationStart(animator: Animator) = Unit
                        override fun onAnimationEnd(animator: Animator) = splashScreen.remove()
                        override fun onAnimationCancel(animator: Animator) = Unit
                        override fun onAnimationRepeat(animator: Animator) = Unit
                    }
                )
                .start()
        }
}