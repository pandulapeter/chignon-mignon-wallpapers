package com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.list

import android.view.LayoutInflater
import android.view.ViewGroup
import com.chignonMignon.wallpapers.presentation.shared.navigation.model.WallpaperDestination
import com.chignonMignon.wallpapers.presentation.utilities.list.BaseListAdapter
import com.chignonMignon.wallpapers.presentation.utilities.list.BaseViewHolder
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.databinding.ItemWallpaperDetailsWallpaperBinding

internal class WallpaperDetailsAdapter(
    private val onSetWallpaperButtonClicked: (WallpaperDestination) -> Unit
) : BaseListAdapter<WallpaperDetailsListItem>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<out WallpaperDetailsListItem, *> = WallpaperViewHolder.create(
        parent = parent,
        onSetWallpaperButtonClicked = onSetWallpaperButtonClicked
    )

    private class WallpaperViewHolder private constructor(
        binding: ItemWallpaperDetailsWallpaperBinding,
        onSetWallpaperButtonClicked: (WallpaperDestination) -> Unit
    ) : BaseViewHolder<WallpaperDetailsListItem.WallpaperUiModel, ItemWallpaperDetailsWallpaperBinding>(binding) {

        init {
            binding.floatingActionButton.setOnClickListener {
                binding.uiModel?.wallpaper?.let(onSetWallpaperButtonClicked)
            }
        }

        override fun bind(listItem: WallpaperDetailsListItem.WallpaperUiModel) {
            binding.uiModel = listItem
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onSetWallpaperButtonClicked: (WallpaperDestination) -> Unit
            ) = WallpaperViewHolder(
                binding = ItemWallpaperDetailsWallpaperBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onSetWallpaperButtonClicked = onSetWallpaperButtonClicked
            )
        }
    }
}