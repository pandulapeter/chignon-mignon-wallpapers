package com.chignonMignon.wallpapers.presentation.collections

import com.chignonMignon.wallpapers.presentation.collections.implementation.CollectionsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationCollectionsModule = module {
    viewModel { CollectionsViewModel(get(), get(), get(), get(), get()) }
}