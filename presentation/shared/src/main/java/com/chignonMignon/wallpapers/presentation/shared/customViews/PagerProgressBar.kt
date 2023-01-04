package com.chignonMignon.wallpapers.presentation.shared.customViews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import kotlin.math.abs

class PagerProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : View(context, attrs) {

    private val paint = Paint().apply {
        style = Paint.Style.FILL
    }
    @get:ColorInt
    var color: Int
        get() = paint.color
        set(@ColorInt value) {
            if (paint.color != value) {
                paint.color = value
                invalidate()
            }
        }
    var progress: Float = 0f
        set(value) {
            if (field != value) {
                field = value
                if (!isLaidOut) {
                    finishAnimation()
                } else {
                    postInvalidateOnAnimation()
                }
            }
        }
    private var animatedProgress = progress

    init {
        setWillNotDraw(false)
    }

    fun finishAnimation() {
        animatedProgress = progress
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val difference = progress - animatedProgress
        val shouldInvalidate = abs(difference) > ANIMATION_LIMIT
        canvas?.run {
            drawRect(0f, 0f, width * (if (shouldInvalidate) animatedProgress else progress), height.toFloat(), paint)
        }
        if (shouldInvalidate) {
            animatedProgress += difference * ANIMATION_SPEED
            postInvalidateOnAnimation()
        }
    }

    companion object {
        private const val ANIMATION_LIMIT = 0.005f
        private const val ANIMATION_SPEED = 0.05f
    }
}