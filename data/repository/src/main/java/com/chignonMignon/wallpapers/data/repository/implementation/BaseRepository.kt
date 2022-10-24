package com.chignonMignon.wallpapers.data.repository.implementation

internal abstract class BaseRepository<T>(
    private val getDataFromSource: suspend () -> T
) {
    private var cache: T? = null

    protected suspend fun getData(isForceRefresh: Boolean) = cache.let { currentCache ->
        if (isForceRefresh || currentCache == null) {
            getDataFromSource().also { cache = it }
        } else {
            currentCache
        }
    }
}