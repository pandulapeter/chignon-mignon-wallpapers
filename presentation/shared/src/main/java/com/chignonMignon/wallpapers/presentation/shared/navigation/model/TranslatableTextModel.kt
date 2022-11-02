package com.chignonMignon.wallpapers.presentation.shared.navigation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TranslatableTextModel(
    val english: String,
    val hungarian: String,
    val romanian: String
) : Parcelable