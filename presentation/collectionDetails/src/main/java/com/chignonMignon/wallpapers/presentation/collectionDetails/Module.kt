package com.chignonMignon.wallpapers.presentation.collectionDetails

import com.chignonMignon.wallpapers.presentation.collectionDetails.implementation.CollectionDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationCollectionDetailsModule = module {
    viewModel { CollectionDetailsViewModel(it[0], get(), get(), get()) }
}