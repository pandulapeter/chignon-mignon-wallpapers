package com.chignonMignon.wallpapers.data.source.remote

import com.chignonMignon.wallpapers.data.source.remote.api.CollectionRemoteSource
import com.chignonMignon.wallpapers.data.source.remote.api.WallpaperRemoteSource
import com.chignonMignon.wallpapers.data.source.remote.implementation.CollectionRemoteSourceImpl
import com.chignonMignon.wallpapers.data.source.remote.implementation.WallpaperRemoteSourceImpl
import com.chignonMignon.wallpapers.data.source.remote.implementation.model.CollectionResponse
import com.chignonMignon.wallpapers.data.source.remote.implementation.model.WallpaperResponse
import com.chignonMignon.wallpapers.data.source.remote.implementation.networking.NetworkManager
import com.github.theapache64.retrosheet.RetrosheetInterceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.converter.moshi.MoshiConverterFactory

val sourceModule = module {
    single {
        RetrosheetInterceptor.Builder()
            .setLogging(true)
            .run {
                CollectionResponse.addSheet(this)
                WallpaperResponse.addSheet(this)
            }
            .run { CollectionResponse.addSheet(this) }
            .build()
    }
    single {
        OkHttpClient.Builder()
            .addInterceptor(get<RetrosheetInterceptor>())
            .build()
    }
    single { MoshiConverterFactory.create() }
    single { NetworkManager(get(), get()).networkingService }
    factory<CollectionRemoteSource> { CollectionRemoteSourceImpl(get()) }
    factory<WallpaperRemoteSource> { WallpaperRemoteSourceImpl(get()) }
}