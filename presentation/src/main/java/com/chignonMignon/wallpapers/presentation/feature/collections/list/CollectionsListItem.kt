package com.chignonMignon.wallpapers.presentation.feature.collections.list

import com.chignonMignon.wallpapers.data.model.domain.Collection
import com.chignonMignon.wallpapers.presentation.utilities.list.ListItem

internal sealed class CollectionsListItem(
    override val id: String
) : ListItem {

    data class CollectionUiModel(
        val collection: Collection
    ) : CollectionsListItem("collection_${collection.id}")
}