package com.chignonMignon.wallpapers.data.source.localImpl.implementation

import com.chignonMignon.wallpapers.data.model.domain.Collection
import com.chignonMignon.wallpapers.data.source.local.CollectionLocalSource
import com.chignonMignon.wallpapers.data.source.localImpl.implementation.database.dao.CollectionDao
import com.chignonMignon.wallpapers.data.source.localImpl.implementation.mapper.toEntity
import com.chignonMignon.wallpapers.data.source.localImpl.implementation.mapper.toModel

internal class CollectionLocalSourceImpl(
    private val collectionDao: CollectionDao
) : CollectionLocalSource {

    override suspend fun getCollections() = collectionDao.getAll().map { it.toModel() }

    override suspend fun saveCollections(collections: List<Collection>) = collectionDao.updateAll(collections.map { it.toEntity() })
}