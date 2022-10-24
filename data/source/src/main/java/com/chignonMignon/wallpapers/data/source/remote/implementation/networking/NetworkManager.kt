package com.chignonMignon.wallpapers.data.source.remote.implementation.networking

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

internal class NetworkManager(
    okHttpClient: OkHttpClient,
    moshiConverterFactory: MoshiConverterFactory
) {
    val networkingService: NetworkingService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(moshiConverterFactory)
        .build()
        .create(NetworkingService::class.java)

    companion object {
        private const val BASE_URL = "https://docs.google.com/spreadsheets/d/1LDCLGX02X2pErmHS6iq0ABZnZJ4i_-TS5ZR5W6-Utv8/"
    }
}