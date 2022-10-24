package com.chignonMignon.wallpapers.presentation

import com.chignonMignon.wallpapers.presentation.feature.about.AboutViewModel
import com.chignonMignon.wallpapers.presentation.feature.collections.CollectionsViewModel
import com.chignonMignon.wallpapers.presentation.feature.collectionDetails.CollectionDetailsViewModel
import com.chignonMignon.wallpapers.presentation.feature.wallpaperDetails.WallpaperDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { AboutViewModel() }
    viewModel { CollectionsViewModel() }
    viewModel { CollectionDetailsViewModel() }
    viewModel { WallpaperDetailsViewModel() }
}