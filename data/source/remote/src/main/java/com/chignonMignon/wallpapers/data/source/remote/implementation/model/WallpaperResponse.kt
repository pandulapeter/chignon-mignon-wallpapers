package com.chignonMignon.wallpapers.data.source.remote.implementation.model

import com.github.theapache64.retrosheet.RetrosheetInterceptor
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class WallpaperResponse(
    @Json(name = KEY_ID) val id: String? = null,
    @Json(name = KEY_COLLECTION_ID) val collectionId: String? = null,
    @Json(name = KEY_NAME_EN) val nameEn: String? = null,
    @Json(name = KEY_NAME_HU) val nameHu: String? = null,
    @Json(name = KEY_NAME_RO) val nameRo: String? = null,
    @Json(name = KEY_THUMBNAIL_URL) val thumbnailUrl: String? = null,
    @Json(name = KEY_URL) val url: String? = null,
    @Json(name = KEY_PRIMARY_COLOR) val primaryColorCode: String? = null,
    @Json(name = KEY_SECONDARY_COLOR) val secondaryColorCode: String? = null,
    @Json(name = KEY_IS_PUBLIC) val isPublic: Boolean? = null
) {
    companion object {
        const val SHEET_NAME = "wallpapers"
        private const val KEY_ID = "id"
        private const val KEY_COLLECTION_ID = "collection_id"
        private const val KEY_NAME_EN = "name_en"
        private const val KEY_NAME_HU = "name_hu"
        private const val KEY_NAME_RO = "name_ro"
        private const val KEY_THUMBNAIL_URL = "thumbnail_url_768px"
        private const val KEY_URL = "url_2880px"
        private const val KEY_PRIMARY_COLOR = "primary_color"
        private const val KEY_SECONDARY_COLOR = "secondary_color"
        private const val KEY_IS_PUBLIC = "is_public"

        internal fun addSheet(interceptorBuilder: RetrosheetInterceptor.Builder) = interceptorBuilder.addSheet(
            SHEET_NAME,
            KEY_ID,
            KEY_COLLECTION_ID,
            KEY_NAME_EN,
            KEY_NAME_HU,
            KEY_NAME_RO,
            KEY_THUMBNAIL_URL,
            KEY_URL,
            KEY_PRIMARY_COLOR,
            KEY_SECONDARY_COLOR,
            KEY_IS_PUBLIC
        )
    }
}