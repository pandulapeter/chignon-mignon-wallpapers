package com.chignonMignon.wallpapers.data.repository.implementation

internal abstract class BaseRepository<T>(
    private val getDataFromRemoteSource: suspend () -> T
) {
    private var cache: T? = null

    protected fun isDataAvailable() = cache != null

    protected suspend fun getData(isForceRefresh: Boolean) = cache.let { currentCache ->
        if (isForceRefresh || currentCache == null) {
            getDataFromRemoteSource().also { cache = it }
        } else {
            currentCache
        }
    }
}