package com.chignonMignon.wallpapers.presentation.feature.collections.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chignonMignon.wallpapers.presentation.databinding.ItemCollectionsCollectionBinding
import com.chignonMignon.wallpapers.presentation.utilities.list.BaseListAdapter
import com.chignonMignon.wallpapers.presentation.utilities.list.BaseViewHolder

internal class CollectionsAdapter(
    private val onItemSelected: (collectionId: String) -> Unit
) : BaseListAdapter<CollectionsListItem>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<out CollectionsListItem, *> =
        CollectionViewHolder.create(parent, onItemSelected)

    private class CollectionViewHolder private constructor(
        binding: ItemCollectionsCollectionBinding,
        onItemSelected: (collectionId: String) -> Unit
    ) : BaseViewHolder<CollectionsListItem.CollectionUiModel, ItemCollectionsCollectionBinding>(binding) {

        init {
            binding.thumbnail.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    binding.uiModel?.collection?.id?.let(onItemSelected)
                }
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onItemSelected: (collectionId: String) -> Unit
            ) = CollectionViewHolder(
                binding = ItemCollectionsCollectionBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onItemSelected = onItemSelected
            )
        }
    }
}