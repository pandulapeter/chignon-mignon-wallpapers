package com.chignonMignon.wallpapers.data.repository.api

import com.chignonMignon.wallpapers.data.model.domain.Collection

interface CollectionRepository {

    suspend fun getCollections(isForceRefresh: Boolean): List<Collection>

    suspend fun getCollectionById(collectionId: String) : Collection
}