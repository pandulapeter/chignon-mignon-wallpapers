package com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.list

import android.view.LayoutInflater
import android.view.ViewGroup
import com.chignonMignon.wallpapers.presentation.utilities.list.BaseListAdapter
import com.chignonMignon.wallpapers.presentation.utilities.list.BaseViewHolder
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.databinding.ItemWallpaperDetailsWallpaperBinding

internal class WallpaperDetailsAdapter : BaseListAdapter<WallpaperDetailsListItem>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<out WallpaperDetailsListItem, *> = WallpaperViewHolder.create(
        parent = parent
    )

    private class WallpaperViewHolder private constructor(
        binding: ItemWallpaperDetailsWallpaperBinding
    ) : BaseViewHolder<WallpaperDetailsListItem.WallpaperUiModel, ItemWallpaperDetailsWallpaperBinding>(binding) {

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