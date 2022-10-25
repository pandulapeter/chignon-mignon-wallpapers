package com.chignonMignon.wallpapers.presentation.feature.collectionDetails.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chignonMignon.wallpapers.presentation.databinding.ItemCollectionDetailsWallpaperBinding
import com.chignonMignon.wallpapers.presentation.utilities.list.BaseListAdapter
import com.chignonMignon.wallpapers.presentation.utilities.list.BaseViewHolder

internal class CollectionDetailsAdapter(
    private val onItemSelected: (wallpaperId: String) -> Unit
) : BaseListAdapter<CollectionDetailsListItem>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<out CollectionDetailsListItem, *> =
        WallpaperViewHolder.create(parent, onItemSelected)

    private class WallpaperViewHolder private constructor(
        binding: ItemCollectionDetailsWallpaperBinding,
        onItemSelected: (wallpaperId: String) -> Unit
    ) : BaseViewHolder<CollectionDetailsListItem.WallpaperUiModel, ItemCollectionDetailsWallpaperBinding>(binding) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    binding.uiModel?.wallpaper?.id?.let(onItemSelected)
                }
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onItemSelected: (wallpaperId: String) -> Unit
            ) = WallpaperViewHolder(
                binding = ItemCollectionDetailsWallpaperBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onItemSelected = onItemSelected
            )
        }
    }
}