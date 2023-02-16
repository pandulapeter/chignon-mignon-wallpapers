package com.chignonMignon.wallpapers.presentation.collections.implementation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.chignonMignon.wallpapers.presentation.collections.R
import com.chignonMignon.wallpapers.presentation.utilities.extensions.color
import com.chignonMignon.wallpapers.presentation.utilities.extensions.dimension
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin

class HintView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val initialStrokeWidth = context.dimension(R.dimen.collections_hint_ripple_wave_size).toFloat()
    private val centerRadius = context.dimension(R.dimen.collections_hint_ripple_center_radius).toFloat()
    private val wavePaint = Paint().apply {
        color = context.color(com.chignonMignon.wallpapers.presentation.shared.R.color.brand_white)
        style = Paint.Style.STROKE
        strokeWidth = initialStrokeWidth
        isAntiAlias = true
    }
    private val centerPaint = Paint().apply {
        color = context.color(com.chignonMignon.wallpapers.presentation.shared.R.color.brand_white)
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    private var progress = 0f
    private var viewCoordinates = intArrayOf(0, 0)
    private var targetCoordinates = intArrayOf(0, 0)
    var radiusMultiplier = 1f
        set(value) {
            field = value
            alpha = VIEW_ALPHA * value
            if (value != 0f) {
                postInvalidateOnAnimation()
            }
        }
    private var alphaMultiplier = 1f
    var targetView: View? = null
        set(value) {
            field = value
            postInvalidateOnAnimation()
        }

    init {
        setWillNotDraw(false)
        alpha = VIEW_ALPHA
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility != VISIBLE) {
            alphaMultiplier = 0f
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (radiusMultiplier > 0f) {
            targetView?.let { targetView ->
                canvas?.run {
                    if (visibility == VISIBLE && alphaMultiplier < 1f) {
                        alphaMultiplier += 0.0025f
                    }
                    getLocationOnScreen(viewCoordinates)
                    targetView.getLocationOnScreen(targetCoordinates)
                    val centerX = targetCoordinates.first() + targetView.width * 0.5f - viewCoordinates.first()
                    val centerY = targetCoordinates.last() + targetView.height * 0.5f - viewCoordinates.last()
                    val maximumRadius = min(width, height) * WAVE_SIZE_MULTIPLIER * radiusMultiplier
                    val waveDistance = 1f / WAVE_COUNT
                    for (i in 0..WAVE_COUNT) {
                        val adjustedProgress = progress.normalize(1.5f * i * waveDistance)
                        val exponentialProgress = adjustedProgress * adjustedProgress
                        val inverseExponentialProgress = 1f - exponentialProgress
                        drawCircle(centerX, centerY, maximumRadius * exponentialProgress, wavePaint.apply {
                            alpha = (inverseExponentialProgress * alphaMultiplier * MAXIMUM_ALPHA).roundToInt()
                            strokeWidth = initialStrokeWidth * inverseExponentialProgress
                        })
                    }
                    drawCircle(centerX, centerY, radiusMultiplier * centerRadius * 0.4f + 0.6f * sin(progress * progress * Math.PI).toFloat(), centerPaint.apply {
                        alpha = (alphaMultiplier * MAXIMUM_ALPHA).roundToInt()
                    })
                    progress += ANIMATION_SPEED
                    if (progress >= 1f) {
                        progress = 0f
                    }
                }
            }
            postInvalidateOnAnimation()
        }
    }

    private fun Float.normalize(extra: Float) = (this + extra).let {
        var number = it
        while (number >= 1f) {
            number -= 1f
        }
        number
    }

    companion object {
        private const val MAXIMUM_ALPHA = 255
        private const val ANIMATION_SPEED = 0.001f
        private const val WAVE_COUNT = 9
        private const val WAVE_SIZE_MULTIPLIER = 1f
        private const val VIEW_ALPHA = 0.35f
    }
}