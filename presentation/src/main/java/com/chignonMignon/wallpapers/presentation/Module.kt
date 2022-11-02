package com.chignonMignon.wallpapers.presentation

import com.chignonMignon.wallpapers.presentation.feature.collectionDetails.CollectionDetailsViewModel
import com.chignonMignon.wallpapers.presentation.feature.collections.CollectionsViewModel
import com.chignonMignon.wallpapers.presentation.feature.wallpaperDetails.WallpaperDetailsViewModel
import com.chignonMignon.wallpapers.presentation.utilities.ColorPaletteGenerator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    single { ColorPaletteGenerator(get()) }
    viewModel { CollectionsViewModel(get(), get(), get()) }
    viewModel { CollectionDetailsViewModel(it[0], get(), get()) }
    viewModel { WallpaperDetailsViewModel(it[0]) }
}