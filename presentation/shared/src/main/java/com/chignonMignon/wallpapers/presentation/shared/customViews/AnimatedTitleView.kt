package com.chignonMignon.wallpapers.presentation.shared.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.ViewSwitcher
import androidx.annotation.StringRes
import com.chignonMignon.wallpapers.presentation.shared.R
import com.chignonMignon.wallpapers.presentation.shared.databinding.ViewAnimatedTitleBinding

class AnimatedTitleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ViewSwitcher(context, attrs) {

    private val currentTextView get() = currentView as TextView
    private val nextTextView get() = nextView as TextView

    init {
        ViewAnimatedTitleBinding.inflate(LayoutInflater.from(context), this)
        clipChildren = false
        inAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_slide_in)
        outAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_slide_out)
        animateFirstView = false
    }

    fun setTitle(@StringRes titleResourceId: Int?) = setTitle(titleResourceId?.let(context::getString) ?: "")

    fun setTitle(title: String?) {
        currentTextView.let { currentTextView ->
            if (currentTextView.text.isNullOrEmpty()) {
                currentTextView.text = title.orEmpty()
            } else {
                nextTextView.text = title.orEmpty()
                showNext()
            }
        }
    }
}