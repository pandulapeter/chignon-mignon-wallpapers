package com.chignonMignon.wallpapers.presentation.shared

import com.chignonMignon.wallpapers.presentation.shared.colorPaletteGenerator.ColorPaletteGenerator
import com.chignonMignon.wallpapers.presentation.shared.colorPaletteGenerator.ColorPaletteGeneratorImpl
import org.koin.dsl.module

val presentationSharedModule = module {
    single<ColorPaletteGenerator> { ColorPaletteGeneratorImpl(get()) }
}