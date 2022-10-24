package com.chignonMignon.wallpapers

import android.animation.Animator
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleSplashScreen()
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