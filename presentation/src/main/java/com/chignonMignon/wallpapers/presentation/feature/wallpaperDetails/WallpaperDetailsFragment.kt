package com.chignonMignon.wallpapers.presentation.feature.wallpaperDetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.chignonMignon.wallpapers.presentation.R
import com.chignonMignon.wallpapers.presentation.databinding.FragmentWallpaperDetailsBinding
import com.chignonMignon.wallpapers.presentation.feature.Navigator
import com.chignonMignon.wallpapers.presentation.utilities.BundleDelegate
import com.chignonMignon.wallpapers.presentation.utilities.bind
import com.chignonMignon.wallpapers.presentation.utilities.withArguments
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class WallpaperDetailsFragment : Fragment(R.layout.fragment_wallpaper_details) {

    private val viewModel by viewModel<WallpaperDetailsViewModel> { parametersOf(arguments?.wallpaper) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = bind<FragmentWallpaperDetailsBinding>(view)
        binding.viewModel = viewModel
    }

    companion object {
        private var Bundle.wallpaper by BundleDelegate.Parcelable<Navigator.Wallpaper>("wallpaper")

        fun newInstance(wallpaper: Navigator.Wallpaper) = WallpaperDetailsFragment().withArguments {
            it.wallpaper = wallpaper
        }
    }
}