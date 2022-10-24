package com.chignonMignon.wallpapers.data.source.remote.implementation.model

import com.github.theapache64.retrosheet.RetrosheetInterceptor
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class WallpaperResponse(
    @Json(name = KEY_ID) val id: String? = null,
    @Json(name = KEY_COLLECTION_ID) val collectionId: String? = null,
    @Json(name = KEY_NAME) val name: String? = null,
    @Json(name = KEY_DESCRIPTION) val description: String? = null,
    @Json(name = KEY_URL) val url: String? = null
) {
    companion object {
        const val SHEET_NAME = "wallpapers"
        private const val KEY_ID = "id"
        private const val KEY_COLLECTION_ID = "collection_id"
        private const val KEY_NAME = "name"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_URL = "url"

        internal fun addSheet(interceptorBuilder: RetrosheetInterceptor.Builder) = interceptorBuilder.addSheet(
            SHEET_NAME,
            KEY_ID,
            KEY_COLLECTION_ID,
            KEY_DESCRIPTION
        )
    }
}