package com.chignonMignon.wallpapers.presentation.wallpaperDetails

import com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.WallpaperDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationWallpaperDetailsModule = module {
    viewModel { WallpaperDetailsViewModel(it[0]) }
}