package com.chignonMignon.wallpapers.presentation.feature.collections.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chignonMignon.wallpapers.presentation.R
import com.chignonMignon.wallpapers.presentation.databinding.ItemCollectionsCollectionBinding
import com.chignonMignon.wallpapers.presentation.databinding.ItemCollectionsWelcomeBinding
import com.chignonMignon.wallpapers.presentation.utilities.list.BaseListAdapter
import com.chignonMignon.wallpapers.presentation.utilities.list.BaseViewHolder

internal class CollectionsAdapter(
    private val onItemSelected: (collectionId: String, sharedElements: List<View>) -> Unit
) : BaseListAdapter<CollectionsListItem>() {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is CollectionsListItem.CollectionUiModel -> R.layout.item_collections_collection
        is CollectionsListItem.WelcomeUiModel -> R.layout.item_collections_welcome
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<out CollectionsListItem, *> = when (viewType) {
        R.layout.item_collections_collection -> CollectionViewHolder.create(parent, onItemSelected)
        R.layout.item_collections_welcome -> WelcomeViewHolder.create(parent)
        else -> throw IllegalArgumentException("Unsupported view type: $viewType")
    }

    private class CollectionViewHolder private constructor(
        binding: ItemCollectionsCollectionBinding,
        onItemSelected: (collectionId: String, sharedElements: List<View>) -> Unit
    ) : BaseViewHolder<CollectionsListItem.CollectionUiModel, ItemCollectionsCollectionBinding>(binding) {

        init {
            binding.root.tag = binding
            binding.thumbnail.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    binding.uiModel?.collection?.id?.let { onItemSelected(it, listOf(binding.thumbnail)) }
                }
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onItemSelected: (collectionId: String, sharedElements: List<View>) -> Unit
            ) = CollectionViewHolder(
                binding = ItemCollectionsCollectionBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onItemSelected = onItemSelected
            )
        }
    }

    private class WelcomeViewHolder private constructor(
        binding: ItemCollectionsWelcomeBinding
    ) : BaseViewHolder<CollectionsListItem.WelcomeUiModel, ItemCollectionsWelcomeBinding>(binding) {

        init {
            binding.root.tag = binding
        }

        companion object {
            fun create(
                parent: ViewGroup
            ) = WelcomeViewHolder(
                binding = ItemCollectionsWelcomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }
}