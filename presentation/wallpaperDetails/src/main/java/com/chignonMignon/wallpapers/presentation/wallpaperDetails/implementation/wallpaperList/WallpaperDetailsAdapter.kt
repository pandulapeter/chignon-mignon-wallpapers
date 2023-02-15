package com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.wallpaperList

import android.animation.LayoutTransition
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import com.chignonMignon.wallpapers.presentation.utilities.extensions.ImageViewTag
import com.chignonMignon.wallpapers.presentation.utilities.list.BaseListAdapter
import com.chignonMignon.wallpapers.presentation.utilities.list.BaseViewHolder
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.databinding.ItemWallpaperDetailsWallpaperBinding


internal class WallpaperDetailsAdapter : BaseListAdapter<WallpaperDetailsListItem>() {

    interface BitmapCallback {

        fun getBitmap(): Bitmap?
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
            binding.root.tag = object : BitmapCallback {

                override fun getBitmap(): Bitmap {
                    binding.preview.isDrawingCacheEnabled = true
                    val bitmap = Bitmap.createBitmap(binding.preview.drawingCache)
                    binding.preview.isDrawingCacheEnabled = false
                    return bitmap
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