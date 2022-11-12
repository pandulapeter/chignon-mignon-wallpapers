package com.chignonMignon.wallpapers.presentation.about

import com.chignonMignon.wallpapers.presentation.about.implementation.AboutViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationAboutModule = module {
    viewModel { AboutViewModel() }
}