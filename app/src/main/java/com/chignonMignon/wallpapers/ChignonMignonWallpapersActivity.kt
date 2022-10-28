package com.chignonMignon.wallpapers

import android.animation.Animator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.chignonMignon.wallpapers.presentation.feature.Navigator
import com.chignonMignon.wallpapers.presentation.feature.collectionDetails.CollectionDetailsFragment
import com.chignonMignon.wallpapers.presentation.feature.collections.CollectionsFragment
import com.chignonMignon.wallpapers.presentation.feature.wallpaperDetails.WallpaperDetailsFragment
import com.chignonMignon.wallpapers.presentation.utilities.extensions.handleReplace

class ChignonMignonWallpapersActivity : AppCompatActivity(R.layout.activity_chignon_mignon_wallpapers), Navigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        handleSplashScreen()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false);
        if (savedInstanceState == null) {
            navigateToCollections()
        }
    }

    override fun navigateToCollections() = supportFragmentManager.handleReplace(
        containerId = R.id.container
    ) {
        CollectionsFragment.newInstance()
    }

    override fun navigateToCollectionDetails(collection: Navigator.Collection, sharedElements: List<View>) = supportFragmentManager.handleReplace(
        containerId = R.id.container,
        sharedElements = sharedElements,
        addToBackStack = true
    ) {
        CollectionDetailsFragment.newInstance(collection)
    }

    override fun navigateToWallpaperDetails(wallpaper: Navigator.Wallpaper, sharedElements: List<View>) = supportFragmentManager.handleReplace(
        containerId = R.id.container,
        sharedElements = sharedElements,
        addToBackStack = true
    ) {
        WallpaperDetailsFragment.newInstance(wallpaper)
    }

    override fun navigateBack() = supportFragmentManager.popBackStack()

    private fun handleSplashScreen() = installSplashScreen().setOnExitAnimationListener { splashScreen ->
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