package com.chignonMignon.wallpapers.data.repository.api

import com.chignonMignon.wallpapers.data.model.domain.Collection

interface CollectionRepository {

    fun areCollectionsAvailable() : Boolean

    suspend fun getCollections(isForceRefresh: Boolean): List<Collection>

    suspend fun getCollectionById(isForceRefresh: Boolean, collectionId: String) : Collection
}