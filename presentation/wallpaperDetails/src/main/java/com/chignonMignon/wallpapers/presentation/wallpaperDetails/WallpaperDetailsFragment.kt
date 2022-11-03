package com.chignonMignon.wallpapers.presentation.wallpaperDetails

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.chignonMignon.wallpapers.presentation.shared.extensions.navigator
import com.chignonMignon.wallpapers.presentation.shared.extensions.showSnackbar
import com.chignonMignon.wallpapers.presentation.shared.navigation.model.WallpaperDestination
import com.chignonMignon.wallpapers.presentation.utilities.BundleDelegate
import com.chignonMignon.wallpapers.presentation.utilities.extensions.bind
import com.chignonMignon.wallpapers.presentation.utilities.extensions.observe
import com.chignonMignon.wallpapers.presentation.utilities.extensions.withArguments
import com.chignonMignon.wallpapers.presentation.utilities.sharedElementTransition
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.databinding.FragmentWallpaperDetailsBinding
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.WallpaperDetailsViewModel
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.list.WallpaperDetailsAdapter
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.setWallpaper
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class WallpaperDetailsFragment : Fragment(R.layout.fragment_wallpaper_details) {

    private val viewModel by viewModel<WallpaperDetailsViewModel> {
        parametersOf(arguments?.wallpapers, arguments?.selectedWallpaperIndex)
    }
    private val wallpaperDetailsAdapter by lazy {
        WallpaperDetailsAdapter(
            onSetWallpaperButtonClicked = { wallpaperDestination ->
                context?.let { viewModel.onSetWallpaperButtonPressed(it, wallpaperDestination) }
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = sharedElementTransition()
        sharedElementReturnTransition = sharedElementTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = bind<FragmentWallpaperDetailsBinding>(view)
        binding.viewModel = viewModel
        binding.setupToolbar()
        binding.setupViewPager()
        viewModel.events.observe(viewLifecycleOwner, ::handleEvent)
        postponeEnterTransition()
        (view.parent as? ViewGroup)?.doOnPreDraw { binding.viewPager.post { startPostponedEnterTransition() } }
    }

    private fun FragmentWallpaperDetailsBinding.setupToolbar() = toolbar.setNavigationOnClickListener {
        navigator?.navigateBack()
    }


    private fun FragmentWallpaperDetailsBinding.setupViewPager() = viewPager.run {
        val viewModel = this@WallpaperDetailsFragment.viewModel
        wallpaperDetailsAdapter.submitList(viewModel.wallpaperListItems)
        adapter = wallpaperDetailsAdapter
        offscreenPageLimit = 1
        setCurrentItem(viewModel.wallpaperListItems.indexOfFirst { it.wallpaper.id == viewModel.focusedWallpaper.value.id }, false)
        registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) = viewModel.onPageSelected(position)
        })
    }

    private fun handleEvent(event: WallpaperDetailsViewModel.Event) = when (event) {
        is WallpaperDetailsViewModel.Event.SetWallpaper -> setWallpaper(event.uri)
        is WallpaperDetailsViewModel.Event.ShowErrorMessage -> showErrorMessage(event.wallpaper)
    }

    private fun setWallpaper(uri: Uri) = context?.setWallpaper(uri)

    private fun showErrorMessage(wallpaper: WallpaperDestination) = context?.let {
        showSnackbar(
            messageResourceId = com.chignonMignon.wallpapers.presentation.shared.R.string.wallpaper_details_cannot_set_wallpaper,
            action = { viewModel.onSetWallpaperButtonPressed(it, wallpaper) }
        )
    }

    companion object {
        private var Bundle.wallpapers by BundleDelegate.ParcelableList<WallpaperDestination>("wallpapers")
        private var Bundle.selectedWallpaperIndex by BundleDelegate.Int("selectedWallpaperIndex")

        fun newInstance(
            wallpapers: List<WallpaperDestination>,
            selectedWallpaperIndex: Int
        ) = WallpaperDetailsFragment().withArguments {
            it.wallpapers = wallpapers
            it.selectedWallpaperIndex = selectedWallpaperIndex
        }
    }
}