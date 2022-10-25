package com.chignonMignon.wallpapers.presentation

import com.chignonMignon.wallpapers.presentation.feature.about.AboutViewModel
import com.chignonMignon.wallpapers.presentation.feature.collectionDetails.CollectionDetailsViewModel
import com.chignonMignon.wallpapers.presentation.feature.collections.CollectionsViewModel
import com.chignonMignon.wallpapers.presentation.feature.shared.ColorPaletteGenerator
import com.chignonMignon.wallpapers.presentation.feature.wallpaperDetails.WallpaperDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    single { ColorPaletteGenerator(get()) }
    viewModel { AboutViewModel() }
    viewModel { CollectionsViewModel(get(), get()) }
    viewModel { CollectionDetailsViewModel(it[0], get(), get()) }
    viewModel { WallpaperDetailsViewModel(it[0]) }
}