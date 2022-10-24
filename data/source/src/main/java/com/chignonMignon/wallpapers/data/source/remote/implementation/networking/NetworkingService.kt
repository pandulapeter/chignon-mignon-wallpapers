package com.chignonMignon.wallpapers.data.source.remote.implementation.networking

import com.chignonMignon.wallpapers.data.source.remote.implementation.model.CollectionResponse
import com.chignonMignon.wallpapers.data.source.remote.implementation.model.WallpaperResponse
import com.github.theapache64.retrosheet.annotations.Read
import retrofit2.http.GET

internal interface NetworkingService {

    @Read
    @GET(CollectionResponse.SHEET_NAME)
    suspend fun getCollections(): List<CollectionResponse>

    @Read
    @GET(WallpaperResponse.SHEET_NAME)
    suspend fun getWallpapers(): List<WallpaperResponse>
}