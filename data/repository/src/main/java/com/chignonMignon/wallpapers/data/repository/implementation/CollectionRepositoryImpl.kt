package com.chignonMignon.wallpapers.data.repository.implementation

import com.chignonMignon.wallpapers.data.model.domain.Collection
import com.chignonMignon.wallpapers.data.repository.api.CollectionRepository
import com.chignonMignon.wallpapers.data.source.local.CollectionLocalSource
import com.chignonMignon.wallpapers.data.source.remote.api.CollectionRemoteSource

internal class CollectionRepositoryImpl(
    collectionLocalSource: CollectionLocalSource,
    collectionRemoteSource: CollectionRemoteSource
) : BaseRepository<List<Collection>>(
    getDataFromLocalSource = collectionLocalSource::getCollections,
    getDataFromRemoteSource = collectionRemoteSource::getCollections,
    saveDataToLocalSource = collectionLocalSource::saveCollections,
), CollectionRepository {

    override fun isDataValid(data: List<Collection>) = data.isNotEmpty()

    override fun areCollectionsAvailable() = isDataAvailable()

    override suspend fun getCollections(isForceRefresh: Boolean) = getData(
        isForceRefresh = isForceRefresh
    )

    override suspend fun getCollectionById(isForceRefresh: Boolean, collectionId: String) = getCollections(
        isForceRefresh = isForceRefresh
    ).first { it.id == collectionId }
}