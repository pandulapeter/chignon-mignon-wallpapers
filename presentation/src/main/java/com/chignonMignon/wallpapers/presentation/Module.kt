package com.chignonMignon.wallpapers.presentation

import com.chignonMignon.wallpapers.presentation.feature.about.AboutViewModel
import com.chignonMignon.wallpapers.presentation.feature.categories.CategoriesViewModel
import com.chignonMignon.wallpapers.presentation.feature.categoryDetails.CategoryDetailsViewModel
import com.chignonMignon.wallpapers.presentation.feature.wallpaperDetails.WallpaperDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { AboutViewModel() }
    viewModel { CategoriesViewModel() }
    viewModel { CategoryDetailsViewModel() }
    viewModel { WallpaperDetailsViewModel() }
}