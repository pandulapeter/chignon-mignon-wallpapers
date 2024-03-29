package com.chignonMignon.wallpapers.data.source.remote

import com.chignonMignon.wallpapers.data.source.remote.api.CollectionRemoteSource
import com.chignonMignon.wallpapers.data.source.remote.api.ProductRemoteSource
import com.chignonMignon.wallpapers.data.source.remote.api.WallpaperRemoteSource
import com.chignonMignon.wallpapers.data.source.remote.implementation.CollectionRemoteSourceImpl
import com.chignonMignon.wallpapers.data.source.remote.implementation.ProductRemoteSourceImpl
import com.chignonMignon.wallpapers.data.source.remote.implementation.WallpaperRemoteSourceImpl
import com.chignonMignon.wallpapers.data.source.remote.implementation.model.CollectionResponse
import com.chignonMignon.wallpapers.data.source.remote.implementation.model.ProductResponse
import com.chignonMignon.wallpapers.data.source.remote.implementation.model.WallpaperResponse
import com.chignonMignon.wallpapers.data.source.remote.implementation.networking.NetworkManager
import com.github.theapache64.retrosheet.RetrosheetInterceptor
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.Protocol
import org.koin.dsl.module
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val dataRemoteSourceModule = module {
    single {
        RetrosheetInterceptor.Builder()
            .run {
                CollectionResponse.addSheet(this)
                ProductResponse.addSheet(this)
                WallpaperResponse.addSheet(this)
            }
            .build()
    }
    single {
        OkHttpClient.Builder()
            .addInterceptor(get<RetrosheetInterceptor>())
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool(0, 1, TimeUnit.NANOSECONDS)).protocols(listOf(Protocol.HTTP_1_1))
            .build()
    }
    single { MoshiConverterFactory.create() }
    single { NetworkManager(get(), get()).networkingService }
    factory<CollectionRemoteSource> { CollectionRemoteSourceImpl(get()) }
    factory<ProductRemoteSource> { ProductRemoteSourceImpl(get()) }
    factory<WallpaperRemoteSource> { WallpaperRemoteSourceImpl(get()) }
}