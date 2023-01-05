package com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.wallpaperList

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.core.view.get
import com.github.chrisbanes.photoview.PhotoView

internal class PhotoViewWrapper @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var isParentInterceptionDisallowed = false
    private val photoView get() = get(0) as PhotoView

    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        isParentInterceptionDisallowed = disallowIntercept
        if (disallowIntercept) {
            parent.requestDisallowInterceptTouchEvent(true)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        parent.requestDisallowInterceptTouchEvent(photoView.scale != 1f && isParentInterceptionDisallowed)
        return false
    }
}