package com.chignonMignon.wallpapers

import android.animation.Animator
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.chignonMignon.wallpapers.presentation.feature.Navigator
import com.chignonMignon.wallpapers.presentation.feature.about.AboutFragment
import com.chignonMignon.wallpapers.presentation.feature.categories.CategoriesFragment
import com.chignonMignon.wallpapers.presentation.feature.categoryDetails.CategoryDetailsFragment
import com.chignonMignon.wallpapers.presentation.feature.wallpaperDetails.WallpaperDetailsFragment
import com.chignonMignon.wallpapers.presentation.utilities.handleReplace

class MainActivity : AppCompatActivity(R.layout.activity_chignon_mignon_wallpapers), Navigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleSplashScreen()
        if (savedInstanceState == null) {
            navigateToCategories()
        }
    }

    override fun navigateToCategories() = supportFragmentManager.handleReplace(R.id.container) {
        CategoriesFragment.newInstance()
    }

    override fun navigateToCategoryDetails(categoryId: String) = supportFragmentManager.handleReplace(R.id.container) {
        CategoryDetailsFragment.newInstance(categoryId)
    }

    override fun navigateToWallpaperDetails(wallpaperId: String) = supportFragmentManager.handleReplace(R.id.container) {
        WallpaperDetailsFragment.newInstance(wallpaperId)
    }

    override fun navigateToAbout() = supportFragmentManager.handleReplace(R.id.container) {
        AboutFragment.newInstance()
    }

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