package com.chignonMignon.wallpapers.data.source.local.implementation

import com.chignonMignon.wallpapers.data.model.domain.Collection
import com.chignonMignon.wallpapers.data.source.local.api.CollectionLocalSource

internal class CollectionLocalSourceImpl(
) : CollectionLocalSource {

    override suspend fun getCollections() = TODO("Not yet implemented")

    override suspend fun saveCollections(collections: List<Collection>) = TODO("Not yet implemented")
}