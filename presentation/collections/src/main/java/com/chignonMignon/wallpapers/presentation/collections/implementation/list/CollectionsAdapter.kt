package com.chignonMignon.wallpapers.presentation.collections.implementation.list

import android.animation.LayoutTransition
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.chignonMignon.wallpapers.presentation.collections.R
import com.chignonMignon.wallpapers.presentation.collections.databinding.ItemCollectionsAboutBinding
import com.chignonMignon.wallpapers.presentation.collections.databinding.ItemCollectionsCollectionBinding
import com.chignonMignon.wallpapers.presentation.collections.databinding.ItemCollectionsEmptyBinding
import com.chignonMignon.wallpapers.presentation.collections.databinding.ItemCollectionsErrorBinding
import com.chignonMignon.wallpapers.presentation.collections.databinding.ItemCollectionsWelcomeBinding
import com.chignonMignon.wallpapers.presentation.utilities.extensions.ImageViewTag
import com.chignonMignon.wallpapers.presentation.utilities.list.BaseListAdapter
import com.chignonMignon.wallpapers.presentation.utilities.list.BaseViewHolder

internal class CollectionsAdapter(
    private val onPreviousPageNavigationHelperClicked: () -> Unit,
    private val onNextPageNavigationHelperClicked: () -> Unit,
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
        R.layout.item_collections_about -> AboutViewHolder.create(parent, onPreviousPageNavigationHelperClicked)
        R.layout.item_collections_collection -> CollectionViewHolder.create(parent, onItemSelected, onPreviousPageNavigationHelperClicked, onNextPageNavigationHelperClicked)
        R.layout.item_collections_empty -> EmptyViewHolder.create(parent, onPreviousPageNavigationHelperClicked)
        R.layout.item_collections_error -> ErrorViewHolder.create(parent, onTryAgainButtonClicked, onPreviousPageNavigationHelperClicked)
        R.layout.item_collections_welcome -> WelcomeViewHolder.create(parent, onNextPageNavigationHelperClicked)
        else -> throw IllegalArgumentException("Unsupported view type: $viewType")
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder<out CollectionsListItem, *>) {
        if (holder is CollectionViewHolder) {
            holder.startAnimation()
        }
    }

    private class AboutViewHolder private constructor(
        binding: ItemCollectionsAboutBinding,
        onPreviousPageNavigationHelperClicked: () -> Unit
    ) : BaseViewHolder<CollectionsListItem.AboutUiModel, ItemCollectionsAboutBinding>(binding) {

        init {
            binding.root.tag = binding
            binding.navigateBackClick.setOnClickListener { onPreviousPageNavigationHelperClicked() }
        }

        override fun bind(listItem: CollectionsListItem.AboutUiModel) {
            binding.uiModel = listItem
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onPreviousPageNavigationHelperClicked: () -> Unit
            ) = AboutViewHolder(
                binding = ItemCollectionsAboutBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onPreviousPageNavigationHelperClicked = onPreviousPageNavigationHelperClicked
            )
        }
    }

    private class CollectionViewHolder private constructor(
        binding: ItemCollectionsCollectionBinding,
        onItemSelected: (collectionId: String, sharedElements: List<View>) -> Unit,
        onPreviousPageNavigationHelperClicked: () -> Unit,
        onNextPageNavigationHelperClicked: () -> Unit
    ) : BaseViewHolder<CollectionsListItem.CollectionUiModel, ItemCollectionsCollectionBinding>(binding) {

        private val thumbnailAnimation = AnimationUtils.loadAnimation(binding.root.context, R.anim.anim_pulsate)

        init {
            val onClickListener = View.OnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    binding.uiModel?.collectionDestination?.id?.let { onItemSelected(it, listOf(binding.thumbnail)) }
                }
            }
            binding.root.tag = binding
            binding.thumbnailImage.tag = ImageViewTag(
                loadingIndicator = binding.loadingIndicator
            )
            binding.thumbnail.setOnClickListener(onClickListener)
            binding.thumbnail.layoutTransition = LayoutTransition().apply { setAnimateParentHierarchy(false) }
            binding.hintView.targetView = binding.thumbnail
            binding.navigateBackClick.setOnClickListener { onPreviousPageNavigationHelperClicked() }
            binding.navigateClick.setOnClickListener(onClickListener)
            binding.navigateForwardClick.setOnClickListener { onNextPageNavigationHelperClicked() }
        }

        override fun bind(listItem: CollectionsListItem.CollectionUiModel) {
            binding.uiModel = listItem
        }

        fun startAnimation() = binding.thumbnail.startAnimation(thumbnailAnimation)

        companion object {
            fun create(
                parent: ViewGroup,
                onItemSelected: (collectionId: String, sharedElements: List<View>) -> Unit,
                onPreviousPageNavigationHelperClicked: () -> Unit,
                onNextPageNavigationHelperClicked: () -> Unit
            ) = CollectionViewHolder(
                binding = ItemCollectionsCollectionBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onItemSelected = onItemSelected,
                onPreviousPageNavigationHelperClicked = onPreviousPageNavigationHelperClicked,
                onNextPageNavigationHelperClicked = onNextPageNavigationHelperClicked
            )
        }
    }

    private class EmptyViewHolder private constructor(
        binding: ItemCollectionsEmptyBinding,
        onPreviousPageNavigationHelperClicked: () -> Unit
    ) : BaseViewHolder<CollectionsListItem.EmptyUiModel, ItemCollectionsEmptyBinding>(binding) {

        init {
            binding.root.tag = binding
            binding.root.setOnClickListener { onPreviousPageNavigationHelperClicked() }
        }

        override fun bind(listItem: CollectionsListItem.EmptyUiModel) {
            binding.uiModel = listItem
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onPreviousPageNavigationHelperClicked: () -> Unit
            ) = EmptyViewHolder(
                binding = ItemCollectionsEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onPreviousPageNavigationHelperClicked = onPreviousPageNavigationHelperClicked
            )
        }
    }

    private class ErrorViewHolder private constructor(
        binding: ItemCollectionsErrorBinding,
        onTryAgainButtonClicked: () -> Unit,
        onPreviousPageNavigationHelperClicked: () -> Unit
    ) : BaseViewHolder<CollectionsListItem.ErrorUiModel, ItemCollectionsErrorBinding>(binding) {

        init {
            binding.errorState.setOnClickListener { onTryAgainButtonClicked() }
            binding.root.setOnClickListener { onPreviousPageNavigationHelperClicked() }
        }

        override fun bind(listItem: CollectionsListItem.ErrorUiModel) {
            binding.uiModel = listItem
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onTryAgainButtonClicked: () -> Unit,
                onPreviousPageNavigationHelperClicked: () -> Unit
            ) = ErrorViewHolder(
                binding = ItemCollectionsErrorBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onTryAgainButtonClicked = onTryAgainButtonClicked,
                onPreviousPageNavigationHelperClicked = onPreviousPageNavigationHelperClicked
            )
        }
    }

    private class WelcomeViewHolder private constructor(
        binding: ItemCollectionsWelcomeBinding,
        onNextPageNavigationHelperClicked: () -> Unit
    ) : BaseViewHolder<CollectionsListItem.WelcomeUiModel, ItemCollectionsWelcomeBinding>(binding) {

        init {
            binding.root.tag = binding
            binding.root.setOnClickListener { onNextPageNavigationHelperClicked() }
        }

        override fun bind(listItem: CollectionsListItem.WelcomeUiModel) {
            binding.uiModel = listItem
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onNextPageNavigationHelperClicked: () -> Unit
            ) = WelcomeViewHolder(
                binding = ItemCollectionsWelcomeBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onNextPageNavigationHelperClicked = onNextPageNavigationHelperClicked
            )
        }
    }
}