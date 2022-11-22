package com.chignonMignon.wallpapers.data.source.local

import com.chignonMignon.wallpapers.data.source.local.api.CollectionLocalSource
import com.chignonMignon.wallpapers.data.source.local.api.ProductLocalSource
import com.chignonMignon.wallpapers.data.source.local.api.WallpaperLocalSource
import com.chignonMignon.wallpapers.data.source.local.implementation.CollectionLocalSourceImpl
import com.chignonMignon.wallpapers.data.source.local.implementation.ProductLocalSourceImpl
import com.chignonMignon.wallpapers.data.source.local.implementation.WallpaperLocalSourceImpl
import org.koin.dsl.module

val localSourceModule = module {
    factory<CollectionLocalSource> { CollectionLocalSourceImpl() }
    factory<ProductLocalSource> { ProductLocalSourceImpl() }
    factory<WallpaperLocalSource> { WallpaperLocalSourceImpl() }
}