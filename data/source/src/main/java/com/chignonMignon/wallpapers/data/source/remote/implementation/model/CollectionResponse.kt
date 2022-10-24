package com.chignonMignon.wallpapers.data.source.remote.implementation.model

import com.github.theapache64.retrosheet.RetrosheetInterceptor
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class CollectionResponse(
    @Json(name = KEY_ID) val id: String? = null,
    @Json(name = KEY_NAME) val name: String? = null,
    @Json(name = KEY_DESCRIPTION) val description: String? = null,
    @Json(name = KEY_THUMBNAIL_URL) val thumbnailUrl: String? = null
) {
    companion object {
        const val SHEET_NAME = "collections"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_THUMBNAIL_URL = "thumbnail_url"

        internal fun addSheet(interceptorBuilder: RetrosheetInterceptor.Builder) = interceptorBuilder.addSheet(
            SHEET_NAME,
            KEY_ID,
            KEY_NAME,
            KEY_DESCRIPTION,
            KEY_THUMBNAIL_URL
        )
    }
}