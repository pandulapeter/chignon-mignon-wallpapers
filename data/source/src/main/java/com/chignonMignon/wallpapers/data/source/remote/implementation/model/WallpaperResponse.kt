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
    @Json(name = KEY_DESCRIPTION_EN) val descriptionEn: String? = null,
    @Json(name = KEY_DESCRIPTION_HU) val descriptionHu: String? = null,
    @Json(name = KEY_DESCRIPTION_RO) val descriptionRo: String? = null,
    @Json(name = KEY_URL) val url: String? = null
) {
    companion object {
        const val SHEET_NAME = "wallpapers"
        private const val KEY_ID = "id"
        private const val KEY_COLLECTION_ID = "collection_id"
        private const val KEY_NAME_EN = "name_en"
        private const val KEY_NAME_HU = "name_hu"
        private const val KEY_NAME_RO = "name_ro"
        private const val KEY_DESCRIPTION_EN = "description_en"
        private const val KEY_DESCRIPTION_HU = "description_hu"
        private const val KEY_DESCRIPTION_RO = "description_ro"
        private const val KEY_URL = "url"

        internal fun addSheet(interceptorBuilder: RetrosheetInterceptor.Builder) = interceptorBuilder.addSheet(
            SHEET_NAME,
            KEY_ID,
            KEY_COLLECTION_ID,
            KEY_NAME_EN,
            KEY_NAME_HU,
            KEY_NAME_RO,
            KEY_DESCRIPTION_EN,
            KEY_DESCRIPTION_HU,
            KEY_DESCRIPTION_RO,
            KEY_URL
        )
    }
}