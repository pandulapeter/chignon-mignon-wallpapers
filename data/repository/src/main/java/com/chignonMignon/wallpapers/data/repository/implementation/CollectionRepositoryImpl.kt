package com.chignonMignon.wallpapers.data.repository.implementation

import com.chignonMignon.wallpapers.data.model.domain.Collection
import com.chignonMignon.wallpapers.data.repository.api.CollectionRepository
import com.chignonMignon.wallpapers.data.source.remote.api.CollectionRemoteSource

internal class CollectionRepositoryImpl(
    collectionRemoteSource: CollectionRemoteSource
) : BaseRepository<List<Collection>>(collectionRemoteSource::getCategories), CollectionRepository {

    override fun areCollectionsAvailable() = isDataAvailable()

    override suspend fun getCollections(isForceRefresh: Boolean) =
        getData(isForceRefresh)

    override suspend fun getCollectionById(isForceRefresh: Boolean, collectionId: String) =
        getCollections(isForceRefresh).first { it.id == collectionId }
}