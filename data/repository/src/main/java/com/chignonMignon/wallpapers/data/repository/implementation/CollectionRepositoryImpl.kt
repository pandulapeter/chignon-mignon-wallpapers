package com.chignonMignon.wallpapers.data.repository.implementation

import com.chignonMignon.wallpapers.data.model.domain.Collection
import com.chignonMignon.wallpapers.data.repository.api.CollectionRepository
import com.chignonMignon.wallpapers.data.source.remote.api.CollectionRemoteSource

internal class CollectionRepositoryImpl(
    collectionRemoteSource: CollectionRemoteSource
) : BaseRepository<List<Collection>>(collectionRemoteSource::getCategories), CollectionRepository {

    override suspend fun getCollections(isForceRefresh: Boolean) = getData(isForceRefresh)
}