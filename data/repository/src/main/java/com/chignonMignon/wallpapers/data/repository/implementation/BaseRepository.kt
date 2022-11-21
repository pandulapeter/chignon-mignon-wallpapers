package com.chignonMignon.wallpapers.data.repository.implementation

internal abstract class BaseRepository<T>(
    private val getDataFromSource: suspend () -> T
) {
    protected var cache: T? = null
        private set

    protected fun isDataAvailable() = cache != null

    protected suspend fun getData(isForceRefresh: Boolean) = cache.let { currentCache ->
        if (isForceRefresh || currentCache == null) {
            getDataFromSource().also { cache = it }
        } else {
            currentCache
        }
    }
}