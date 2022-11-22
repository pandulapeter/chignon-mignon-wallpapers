package com.chignonMignon.wallpapers.data.source.remote.api

import com.chignonMignon.wallpapers.data.model.domain.Collection

interface CollectionRemoteSource {

    suspend fun getCollections(): List<Collection>
}