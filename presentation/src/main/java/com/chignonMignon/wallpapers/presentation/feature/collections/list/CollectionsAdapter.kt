package com.chignonMignon.wallpapers.presentation.feature.collections.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chignonMignon.wallpapers.presentation.R
import com.chignonMignon.wallpapers.presentation.databinding.ItemCollectionsCollectionBinding
import com.chignonMignon.wallpapers.presentation.databinding.ItemCollectionsEmptyBinding
import com.chignonMignon.wallpapers.presentation.databinding.ItemCollectionsErrorBinding
import com.chignonMignon.wallpapers.presentation.databinding.ItemCollectionsWelcomeBinding
import com.chignonMignon.wallpapers.presentation.utilities.list.BaseListAdapter
import com.chignonMignon.wallpapers.presentation.utilities.list.BaseViewHolder

internal class CollectionsAdapter(
    private val onItemSelected: (collectionId: String, sharedElements: List<View>) -> Unit,
    private val onTryAgainButtonClicked: () -> Unit
) : BaseListAdapter<CollectionsListItem>() {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is CollectionsListItem.CollectionUiModel -> R.layout.item_collections_collection
        is CollectionsListItem.WelcomeUiModel -> R.layout.item_collections_welcome
        is CollectionsListItem.EmptyUiModel -> R.layout.item_collections_empty
        is CollectionsListItem.ErrorUiModel -> R.layout.item_collections_error
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<out CollectionsListItem, *> = when (viewType) {
        R.layout.item_collections_collection -> CollectionViewHolder.create(parent, onItemSelected)
        R.layout.item_collections_welcome -> WelcomeViewHolder.create(parent)
        R.layout.item_collections_empty -> EmptyViewHolder.create(parent)
        R.layout.item_collections_error -> ErrorViewHolder.create(parent, onTryAgainButtonClicked)
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

    private class EmptyViewHolder private constructor(
        binding: ItemCollectionsEmptyBinding
    ) : BaseViewHolder<CollectionsListItem.EmptyUiModel, ItemCollectionsEmptyBinding>(binding) {

        init {
            binding.root.tag = binding
        }

        companion object {
            fun create(
                parent: ViewGroup
            ) = EmptyViewHolder(
                binding = ItemCollectionsEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    private class ErrorViewHolder private constructor(
        binding: ItemCollectionsErrorBinding,
        onTryAgainButtonClicked: () -> Unit
    ) : BaseViewHolder<CollectionsListItem.ErrorUiModel, ItemCollectionsErrorBinding>(binding) {

        init {
            binding.root.tag = binding
            binding.errorState.setOnClickListener { onTryAgainButtonClicked() }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onTryAgainButtonClicked: () -> Unit
            ) = ErrorViewHolder(
                binding = ItemCollectionsErrorBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onTryAgainButtonClicked = onTryAgainButtonClicked
            )
        }
    }
}