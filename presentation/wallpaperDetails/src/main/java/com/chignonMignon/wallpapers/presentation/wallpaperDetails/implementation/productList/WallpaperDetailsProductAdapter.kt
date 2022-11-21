package com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.productList

import android.animation.LayoutTransition
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chignonMignon.wallpapers.data.model.domain.Product
import com.chignonMignon.wallpapers.presentation.utilities.extensions.ImageViewTag
import com.chignonMignon.wallpapers.presentation.utilities.list.BaseListAdapter
import com.chignonMignon.wallpapers.presentation.utilities.list.BaseViewHolder
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.R
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.databinding.ItemWallpaperDetailsProductsEmptyBinding
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.databinding.ItemWallpaperDetailsProductsProductBinding

internal class WallpaperDetailsProductAdapter(
    private val onProductSelected: (Product) -> Unit
) : BaseListAdapter<WallpaperDetailsProductListItem>() {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is WallpaperDetailsProductListItem.ProductUiModel -> R.layout.item_wallpaper_details_products_product
        is WallpaperDetailsProductListItem.EmptyUiModel -> R.layout.item_wallpaper_details_products_empty
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<out WallpaperDetailsProductListItem, *> = when (viewType) {
        R.layout.item_wallpaper_details_products_product -> ProductViewHolder.create(
            parent = parent,
            onProductSelected = onProductSelected
        )
        R.layout.item_wallpaper_details_products_empty -> EmptyViewHolder.create(
            parent = parent
        )
        else -> throw IllegalArgumentException("Unsupported view type: $viewType")
    }

    private class ProductViewHolder private constructor(
        binding: ItemWallpaperDetailsProductsProductBinding,
        onProductSelected: (Product) -> Unit
    ) : BaseViewHolder<WallpaperDetailsProductListItem.ProductUiModel, ItemWallpaperDetailsProductsProductBinding>(binding) {

        init {
            binding.thumbnail.tag = ImageViewTag(
                loadingIndicator = binding.loadingIndicator
            )
            binding.constraintLayout.layoutTransition = LayoutTransition().apply {
                setAnimateParentHierarchy(false)
            }
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    binding.uiModel?.product?.let(onProductSelected)
                }
            }
        }

        override fun bind(listItem: WallpaperDetailsProductListItem.ProductUiModel) {
            binding.uiModel = listItem
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onProductSelected: (Product) -> Unit
            ) = ProductViewHolder(
                binding = ItemWallpaperDetailsProductsProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onProductSelected = onProductSelected
            )
        }
    }

    private class EmptyViewHolder private constructor(
        binding: ItemWallpaperDetailsProductsEmptyBinding
    ) : BaseViewHolder<WallpaperDetailsProductListItem.EmptyUiModel, ItemWallpaperDetailsProductsEmptyBinding>(binding) {

        override fun bind(listItem: WallpaperDetailsProductListItem.EmptyUiModel) {
            binding.uiModel = listItem
        }

        companion object {
            fun create(
                parent: ViewGroup
            ) = EmptyViewHolder(
                binding = ItemWallpaperDetailsProductsEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }
}