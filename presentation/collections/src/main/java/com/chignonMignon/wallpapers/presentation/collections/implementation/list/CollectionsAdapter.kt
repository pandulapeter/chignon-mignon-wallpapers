package com.chignonMignon.wallpapers.presentation.collections.implementation.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chignonMignon.wallpapers.presentation.collections.R
import com.chignonMignon.wallpapers.presentation.collections.databinding.ItemCollectionsAboutBinding
import com.chignonMignon.wallpapers.presentation.collections.databinding.ItemCollectionsCollectionBinding
import com.chignonMignon.wallpapers.presentation.collections.databinding.ItemCollectionsEmptyBinding
import com.chignonMignon.wallpapers.presentation.collections.databinding.ItemCollectionsErrorBinding
import com.chignonMignon.wallpapers.presentation.collections.databinding.ItemCollectionsWelcomeBinding
import com.chignonMignon.wallpapers.presentation.utilities.list.BaseListAdapter
import com.chignonMignon.wallpapers.presentation.utilities.list.BaseViewHolder

internal class CollectionsAdapter(
    private val onItemSelected: (collectionId: String, sharedElements: List<View>) -> Unit,
    private val onTryAgainButtonClicked: () -> Unit
) : BaseListAdapter<CollectionsListItem>() {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is CollectionsListItem.AboutUiModel -> R.layout.item_collections_about
        is CollectionsListItem.CollectionUiModel -> R.layout.item_collections_collection
        is CollectionsListItem.EmptyUiModel -> R.layout.item_collections_empty
        is CollectionsListItem.ErrorUiModel -> R.layout.item_collections_error
        is CollectionsListItem.WelcomeUiModel -> R.layout.item_collections_welcome
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<out CollectionsListItem, *> = when (viewType) {
        R.layout.item_collections_about -> AboutViewHolder.create(parent)
        R.layout.item_collections_collection -> CollectionViewHolder.create(parent, onItemSelected)
        R.layout.item_collections_empty -> EmptyViewHolder.create(parent)
        R.layout.item_collections_error -> ErrorViewHolder.create(parent, onTryAgainButtonClicked)
        R.layout.item_collections_welcome -> WelcomeViewHolder.create(parent)
        else -> throw IllegalArgumentException("Unsupported view type: $viewType")
    }

    private class AboutViewHolder private constructor(
        binding: ItemCollectionsAboutBinding
    ) : BaseViewHolder<CollectionsListItem.AboutUiModel, ItemCollectionsAboutBinding>(binding) {

        init {
            binding.root.tag = binding
        }

        override fun bind(listItem: CollectionsListItem.AboutUiModel) {
            binding.uiModel = listItem
        }

        companion object {
            fun create(
                parent: ViewGroup,
            ) = AboutViewHolder(
                binding = ItemCollectionsAboutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    private class CollectionViewHolder private constructor(
        binding: ItemCollectionsCollectionBinding,
        onItemSelected: (collectionId: String, sharedElements: List<View>) -> Unit
    ) : BaseViewHolder<CollectionsListItem.CollectionUiModel, ItemCollectionsCollectionBinding>(binding) {

        init {
            binding.root.tag = binding
            binding.thumbnail.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    binding.uiModel?.collectionDestination?.id?.let { onItemSelected(it, listOf(binding.thumbnail)) }
                }
            }
        }

        override fun bind(listItem: CollectionsListItem.CollectionUiModel) {
            binding.uiModel = listItem
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

    private class EmptyViewHolder private constructor(
        binding: ItemCollectionsEmptyBinding
    ) : BaseViewHolder<CollectionsListItem.EmptyUiModel, ItemCollectionsEmptyBinding>(binding) {

        override fun bind(listItem: CollectionsListItem.EmptyUiModel) {
            binding.uiModel = listItem
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
            binding.errorState.setOnClickListener { onTryAgainButtonClicked() }
        }

        override fun bind(listItem: CollectionsListItem.ErrorUiModel) {
            binding.uiModel = listItem
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

    private class WelcomeViewHolder private constructor(
        binding: ItemCollectionsWelcomeBinding
    ) : BaseViewHolder<CollectionsListItem.WelcomeUiModel, ItemCollectionsWelcomeBinding>(binding) {

        init {
            binding.root.tag = binding
        }

        override fun bind(listItem: CollectionsListItem.WelcomeUiModel) {
            binding.uiModel = listItem
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