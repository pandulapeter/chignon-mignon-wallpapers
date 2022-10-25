package com.chignonMignon.wallpapers.presentation.feature.wallpaperDetails

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowInsets
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.chignonMignon.wallpapers.presentation.R
import com.chignonMignon.wallpapers.presentation.databinding.FragmentWallpaperDetailsBinding
import com.chignonMignon.wallpapers.presentation.feature.Navigator
import com.chignonMignon.wallpapers.presentation.utilities.BundleDelegate
import com.chignonMignon.wallpapers.presentation.utilities.extensions.bind
import com.chignonMignon.wallpapers.presentation.utilities.extensions.navigator
import com.chignonMignon.wallpapers.presentation.utilities.extensions.observe
import com.chignonMignon.wallpapers.presentation.utilities.extensions.setWallpaper
import com.chignonMignon.wallpapers.presentation.utilities.extensions.showSnackbar
import com.chignonMignon.wallpapers.presentation.utilities.extensions.withArguments
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class WallpaperDetailsFragment : Fragment(R.layout.fragment_wallpaper_details) {

    private val viewModel by viewModel<WallpaperDetailsViewModel> { parametersOf(arguments?.wallpaper) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = bind<FragmentWallpaperDetailsBinding>(view)
        binding.viewModel = viewModel
        binding.setupToolbar()
        binding.setupFloatingActionButton()
        viewModel.events.observe(viewLifecycleOwner, ::handleEvent)
    }

    private fun FragmentWallpaperDetailsBinding.setupToolbar() = toolbar.setNavigationOnClickListener {
        navigator?.navigateBack()
    }

    private fun FragmentWallpaperDetailsBinding.setupFloatingActionButton() = floatingActionButton.setOnApplyWindowInsetsListener { _, insets ->
        val systemBarInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        floatingActionButton.layoutParams = (floatingActionButton.layoutParams as MarginLayoutParams).apply {
            bottomMargin += systemBarInsets.bottom
        }
        WindowInsets.CONSUMED
    }

    private fun handleEvent(event: WallpaperDetailsViewModel.Event) = when (event) {
        is WallpaperDetailsViewModel.Event.SetWallpaper -> setWallpaper(event.uri)
        WallpaperDetailsViewModel.Event.ShowErrorMessage -> showErrorMessage()
    }

    private fun setWallpaper(uri: Uri) = context?.setWallpaper(uri)

    private fun showErrorMessage() = context?.let { showSnackbar { viewModel.onSetWallpaperButtonPressed(it) } }

    companion object {
        private var Bundle.wallpaper by BundleDelegate.Parcelable<Navigator.Wallpaper>("wallpaper")

        fun newInstance(wallpaper: Navigator.Wallpaper) = WallpaperDetailsFragment().withArguments {
            it.wallpaper = wallpaper
        }
    }
}