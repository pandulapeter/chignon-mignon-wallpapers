package com.chignonMignon.wallpapers.presentation.shared

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.LibraryGlideModule
import okhttp3.OkHttpClient
import org.koin.java.KoinJavaComponent
import java.io.InputStream

@Suppress("unused")
@GlideModule
private class ImageLoader : LibraryGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(KoinJavaComponent.getKoin().get<OkHttpClient>())
        )
    }
}