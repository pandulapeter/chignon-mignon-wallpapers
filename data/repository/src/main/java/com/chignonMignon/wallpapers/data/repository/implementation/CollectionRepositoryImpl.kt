package com.chignonMignon.wallpapers.data.repository.implementation

import com.chignonMignon.wallpapers.data.repository.api.CollectionRepository
import com.chignonMignon.wallpapers.data.source.remote.api.CollectionRemoteSource

internal class CollectionRepositoryImpl(
    private val collectionRemoteSource: CollectionRemoteSource
) : CollectionRepository {

    override suspend fun getCollections() = collectionRemoteSource.getCategories()
}