package com.chignonMignon.wallpapers.presentation.shared.extensions

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.RectF
import android.graphics.Shader
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.createBitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.util.Util
import java.nio.ByteBuffer
import java.security.MessageDigest

internal class RoundedCornersTransformation(
    private val topLeft: Float = 0f,
    private val topRight: Float = 0f,
    private val bottomRight: Float = 0f,
    private val bottomLeft: Float = 0f
) : BitmapTransformation() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

    override fun equals(other: Any?) = if (other is RoundedCornersTransformation) {
        topLeft == other.topLeft && topRight == other.topRight && bottomRight == other.bottomRight && bottomLeft == other.bottomLeft
    } else false

    override fun hashCode(): Int {
        var hashCode = Util.hashCode(ID.hashCode(), Util.hashCode(topLeft))
        hashCode = Util.hashCode(topRight, hashCode)
        hashCode = Util.hashCode(bottomRight, hashCode)
        return Util.hashCode(bottomLeft, hashCode)
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
        val radiusData = ByteBuffer.allocate(16)
            .putFloat(topLeft)
            .putFloat(topRight)
            .putFloat(bottomRight)
            .putFloat(bottomLeft)
            .array()
        messageDigest.update(radiusData)
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val output = createBitmap(outWidth, outHeight, toTransform.config)
        output.applyCanvas {
            drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            val matrix = Matrix()
            matrix.setTranslate((outWidth - toTransform.width) / 2f, (outHeight - toTransform.height) / 2f)
            val shader = BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            shader.setLocalMatrix(matrix)
            paint.shader = shader
            val radii = floatArrayOf(
                topLeft, topLeft,
                topRight, topRight,
                bottomRight, bottomRight,
                bottomLeft, bottomLeft,
            )
            val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
            val path = Path().apply { addRoundRect(rect, radii, Path.Direction.CW) }
            drawPath(path, paint)
        }
        return output
    }

    companion object {
        private const val ID = "com.chignonMignon.wallpapers.presentation.shared.extensions.RoundedCornersTransformation"
        private val ID_BYTES = ID.toByteArray(CHARSET)
    }
}