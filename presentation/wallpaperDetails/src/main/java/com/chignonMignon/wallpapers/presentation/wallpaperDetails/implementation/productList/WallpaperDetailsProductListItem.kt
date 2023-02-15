package com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.productList

import com.chignonMignon.wallpapers.data.model.domain.Product
import com.chignonMignon.wallpapers.presentation.utilities.list.ListItem

internal sealed class WallpaperDetailsProductListItem(override val id: String) : ListItem {

    data class ProductUiModel(
        val product: Product
    ) : WallpaperDetailsProductListItem("product_${product.id}")
}