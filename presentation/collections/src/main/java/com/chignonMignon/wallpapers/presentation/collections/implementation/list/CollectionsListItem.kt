package com.chignonMignon.wallpapers.presentation.collections.implementation.list

import com.chignonMignon.wallpapers.presentation.shared.navigation.model.CollectionDestination
import com.chignonMignon.wallpapers.presentation.utilities.list.ListItem

internal sealed class CollectionsListItem(
    override val id: String
) : ListItem {

    data class AboutUiModel(
        val nothing: Any? = null
    ) : CollectionsListItem("about")

    data class CollectionUiModel(
        val collectionDestination: CollectionDestination
    ) : CollectionsListItem("collection_${collectionDestination.id}")

    data class EmptyUiModel(
        val nothing: Any? = null
    ) : CollectionsListItem("empty")

    data class WelcomeUiModel(
        val isHintVisible: Boolean,
        val isErrorVisible: Boolean
    ) : CollectionsListItem("welcome")
}