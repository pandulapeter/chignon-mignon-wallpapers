package com.chignonMignon.wallpapers.data.source.remote.implementation.model

import com.github.theapache64.retrosheet.RetrosheetInterceptor
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ProductResponse(
    @Json(name = KEY_WALLPAPER_ID) val wallpaperId: String? = null,
    @Json(name = KEY_THUMBNAIL_URL) val thumbnailUrl: String? = null,
    @Json(name = KEY_URL) val url: String? = null,
    @Json(name = KEY_IS_PUBLIC) val isPublic: Boolean? = null
) {
    companion object {
        const val SHEET_NAME = "products"
        private const val KEY_WALLPAPER_ID = "wallpaper_id"
        private const val KEY_THUMBNAIL_URL = "thumbnail_url"
        private const val KEY_URL = "url"
        private const val KEY_IS_PUBLIC = "is_public"

        internal fun addSheet(interceptorBuilder: RetrosheetInterceptor.Builder) = interceptorBuilder.addSheet(
            SHEET_NAME,
            KEY_WALLPAPER_ID,
            KEY_THUMBNAIL_URL,
            KEY_URL,
            KEY_IS_PUBLIC
        )
    }
}