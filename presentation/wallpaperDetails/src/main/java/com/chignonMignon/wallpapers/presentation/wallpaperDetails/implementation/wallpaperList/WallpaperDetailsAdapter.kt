package com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.wallpaperList

import android.animation.LayoutTransition
import android.graphics.Matrix
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.ViewGroup
import com.chignonMignon.wallpapers.presentation.utilities.extensions.ImageViewTag
import com.chignonMignon.wallpapers.presentation.utilities.list.BaseListAdapter
import com.chignonMignon.wallpapers.presentation.utilities.list.BaseViewHolder
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.databinding.ItemWallpaperDetailsWallpaperBinding
import kotlin.math.abs
import kotlin.math.roundToInt


internal class WallpaperDetailsAdapter : BaseListAdapter<WallpaperDetailsListItem>() {

    interface GetCropRectCallback {
        fun getCropRect(): Rect
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<out WallpaperDetailsListItem, *> = WallpaperViewHolder.create(
        parent = parent
    )

    private class WallpaperViewHolder private constructor(
        binding: ItemWallpaperDetailsWallpaperBinding
    ) : BaseViewHolder<WallpaperDetailsListItem.WallpaperUiModel, ItemWallpaperDetailsWallpaperBinding>(binding) {

        init {
            binding.preview.tag = ImageViewTag(
                loadingIndicator = binding.loadingIndicator
            )
            binding.root.tag = object : GetCropRectCallback {
                override fun getCropRect(): Rect {
                    val matrix = Matrix()
                    binding.preview.getDisplayMatrix(matrix)
                    val values = FloatArray(9)
                    matrix.getValues(values)
                    val scale = binding.preview.scale
                    val transitionX = abs(values[Matrix.MTRANS_X])
                    val transitionY = abs(values[Matrix.MTRANS_Y])
                    return Rect(
                        (transitionX / scale).roundToInt(),
                        (transitionY / scale).roundToInt(),
                        ((transitionX + binding.preview.width) / scale).roundToInt(),
                        ((transitionY + binding.preview.height) / scale).roundToInt()
                    )
                }
            }
            binding.container.layoutTransition = LayoutTransition().apply {
                setAnimateParentHierarchy(false)
            }
        }

        override fun bind(listItem: WallpaperDetailsListItem.WallpaperUiModel) {
            binding.uiModel = listItem
        }

        companion object {
            fun create(
                parent: ViewGroup
            ) = WallpaperViewHolder(
                binding = ItemWallpaperDetailsWallpaperBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }
}