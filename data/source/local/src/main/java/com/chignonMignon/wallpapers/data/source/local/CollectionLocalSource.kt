package com.chignonMignon.wallpapers.data.source.local

import com.chignonMignon.wallpapers.data.model.domain.Collection

interface CollectionLocalSource {

    suspend fun getCollections(): List<Collection>

    suspend fun saveCollections(collections: List<Collection>)
}