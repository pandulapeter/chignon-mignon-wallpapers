package com.chignonMignon.wallpapers.presentation.collectionDetails.implementation.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chignonMignon.wallpapers.presentation.collectionDetails.databinding.ItemCollectionDetailsWallpaperBinding
import com.chignonMignon.wallpapers.presentation.utilities.list.BaseListAdapter
import com.chignonMignon.wallpapers.presentation.utilities.list.BaseViewHolder

internal class CollectionDetailsAdapter(
    private val onItemSelected: (wallpaperId: String, sharedElements: List<View>) -> Unit
) : BaseListAdapter<CollectionDetailsListItem>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<out CollectionDetailsListItem, *> =
        WallpaperViewHolder.create(parent, onItemSelected)

    private class WallpaperViewHolder private constructor(
        binding: ItemCollectionDetailsWallpaperBinding,
        onItemSelected: (wallpaperId: String, sharedElements: List<View>) -> Unit
    ) : BaseViewHolder<CollectionDetailsListItem.WallpaperUiModel, ItemCollectionDetailsWallpaperBinding>(binding) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    binding.uiModel?.wallpaperDestination?.id?.let { onItemSelected(it, listOf(binding.root)) }
                }
            }
        }

        override fun bind(listItem: CollectionDetailsListItem.WallpaperUiModel) {
            binding.uiModel = listItem
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onItemSelected: (wallpaperId: String, sharedElements: List<View>) -> Unit
            ) = WallpaperViewHolder(
                binding = ItemCollectionDetailsWallpaperBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onItemSelected = onItemSelected
            )
        }
    }
}