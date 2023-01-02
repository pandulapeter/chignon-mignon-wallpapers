package com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.wallpaperList

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.annotation.AttrRes

class PhotoViewWrapper @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var isParentInterceptionDisallowed = false

    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        isParentInterceptionDisallowed = disallowIntercept
        if (disallowIntercept) {
            parent.requestDisallowInterceptTouchEvent(true)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val isMultiTouch = ev.pointerCount > 1
        parent.requestDisallowInterceptTouchEvent(isParentInterceptionDisallowed || isMultiTouch)
        return false
    }
}