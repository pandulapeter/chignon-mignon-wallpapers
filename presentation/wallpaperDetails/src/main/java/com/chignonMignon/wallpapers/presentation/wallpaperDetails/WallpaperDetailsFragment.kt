package com.chignonMignon.wallpapers.presentation.wallpaperDetails

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.WallpaperDetailsViewModel
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.setWallpaper
import com.chignonMignon.wallpapers.presentation.shared.extensions.navigator
import com.chignonMignon.wallpapers.presentation.shared.extensions.showSnackbar
import com.chignonMignon.wallpapers.presentation.shared.navigation.model.WallpaperDestination
import com.chignonMignon.wallpapers.presentation.utilities.BundleDelegate
import com.chignonMignon.wallpapers.presentation.utilities.extensions.bind
import com.chignonMignon.wallpapers.presentation.utilities.extensions.observe
import com.chignonMignon.wallpapers.presentation.utilities.extensions.withArguments
import com.chignonMignon.wallpapers.presentation.utilities.sharedElementTransition
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.databinding.FragmentWallpaperDetailsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class WallpaperDetailsFragment : Fragment(R.layout.fragment_wallpaper_details) {

    private val viewModel by viewModel<WallpaperDetailsViewModel> { parametersOf(arguments?.wallpaper) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = sharedElementTransition()
        sharedElementReturnTransition = sharedElementTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = bind<FragmentWallpaperDetailsBinding>(view)
        binding.viewModel = viewModel
        binding.setupToolbar()
        binding.setupFloatingActionButton()
        viewModel.events.observe(viewLifecycleOwner, ::handleEvent)
        postponeEnterTransition()
        (view.parent as? ViewGroup)?.doOnPreDraw { binding.preview.post { startPostponedEnterTransition() } }
    }

    private fun FragmentWallpaperDetailsBinding.setupToolbar() = toolbar.setNavigationOnClickListener {
        navigator?.navigateBack()
    }

    private fun FragmentWallpaperDetailsBinding.setupFloatingActionButton() {
        floatingActionButton.run { postDelayed({ show() }, 500) }
    }

    private fun handleEvent(event: WallpaperDetailsViewModel.Event) = when (event) {
        is WallpaperDetailsViewModel.Event.SetWallpaper -> setWallpaper(event.uri)
        WallpaperDetailsViewModel.Event.ShowErrorMessage -> showErrorMessage()
    }

    private fun setWallpaper(uri: Uri) = context?.setWallpaper(uri)

    private fun showErrorMessage() = context?.let { showSnackbar { viewModel.onSetWallpaperButtonPressed(it) } }

    companion object {
        private var Bundle.wallpaper by BundleDelegate.Parcelable<WallpaperDestination>("wallpaper")

        fun newInstance(wallpaper: WallpaperDestination) = WallpaperDetailsFragment().withArguments {
            it.wallpaper = wallpaper
        }
    }
}