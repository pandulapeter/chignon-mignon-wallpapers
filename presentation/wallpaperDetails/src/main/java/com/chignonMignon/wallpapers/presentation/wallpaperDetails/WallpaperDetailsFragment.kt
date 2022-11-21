package com.chignonMignon.wallpapers.presentation.wallpaperDetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.chignonMignon.wallpapers.presentation.shared.extensions.navigator
import com.chignonMignon.wallpapers.presentation.shared.extensions.showSnackbar
import com.chignonMignon.wallpapers.presentation.shared.navigation.model.WallpaperDestination
import com.chignonMignon.wallpapers.presentation.utilities.BundleDelegate
import com.chignonMignon.wallpapers.presentation.utilities.ColorTransitionManager
import com.chignonMignon.wallpapers.presentation.utilities.extensions.autoClearedValue
import com.chignonMignon.wallpapers.presentation.utilities.extensions.bind
import com.chignonMignon.wallpapers.presentation.utilities.extensions.color
import com.chignonMignon.wallpapers.presentation.utilities.extensions.delaySharedElementTransition
import com.chignonMignon.wallpapers.presentation.utilities.extensions.observe
import com.chignonMignon.wallpapers.presentation.utilities.extensions.setupTransitions
import com.chignonMignon.wallpapers.presentation.utilities.extensions.withArguments
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.databinding.FragmentWallpaperDetailsBinding
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.WallpaperDetailsViewModel
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.productList.WallpaperDetailsProductAdapter
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.setWallpaper
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.wallpaperList.WallpaperDetailsAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class WallpaperDetailsFragment : Fragment(R.layout.fragment_wallpaper_details) {

    private val viewModel by viewModel<WallpaperDetailsViewModel> {
        parametersOf(arguments?.wallpapers, arguments?.selectedWallpaperIndex)
    }
    private val wallpaperDetailsAdapter by lazy { WallpaperDetailsAdapter() }
    private var binding by autoClearedValue<FragmentWallpaperDetailsBinding>()
    private val primaryColorTransitionManager by lazy {
        ColorTransitionManager(requireContext().color(com.chignonMignon.wallpapers.presentation.shared.R.color.primary), viewModel::updatePrimaryColor)
    }
    private var shouldAnimateColorTransitions = false
    private val productAdapter by lazy {
        WallpaperDetailsProductAdapter(
            onProductSelected = viewModel::onProductSelected
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTransitions()
        setEnterSharedElementCallback(
            object : SharedElementCallback() {
                override fun onMapSharedElements(names: List<String?>, sharedElements: MutableMap<String?, View?>) {
                    binding.root.let { sharedElementView ->
                        if (names.isEmpty() || sharedElementView.transitionName == null) {
                            sharedElements.clear()
                        } else {
                            sharedElements[names[0]] = sharedElementView
                        }
                    }
                }
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = bind(view)
        binding.viewModel = viewModel
        binding.setupToolbar()
        binding.setupViewPager()
        binding.setupRecyclerView()
        viewModel.focusedWallpaper.observe(viewLifecycleOwner, ::onFocusedWallpaperChanged)
        viewModel.productListItems.observe(viewLifecycleOwner, productAdapter::submitList)
        viewModel.events.observe(viewLifecycleOwner, ::handleEvent)
        delaySharedElementTransition(binding.viewPager)
    }

    override fun onResume() {
        super.onResume()
        binding.progressBar.finishAnimation()
    }

    override fun onStop() {
        super.onStop()
        navigator?.selectedWallpaperIndex = binding.viewPager.currentItem
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

    private fun FragmentWallpaperDetailsBinding.setupRecyclerView() = recyclerView.run {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(
            context,
            if (context.resources.getBoolean(com.chignonMignon.wallpapers.presentation.shared.R.bool.is_landscape)) LinearLayoutManager.VERTICAL else LinearLayoutManager.HORIZONTAL,
            false
        )
        adapter = productAdapter
    }

    private fun handleEvent(event: WallpaperDetailsViewModel.Event) = when (event) {
        is WallpaperDetailsViewModel.Event.SetWallpaper -> setWallpaper(event.uri)
        is WallpaperDetailsViewModel.Event.ShowErrorMessage -> showErrorMessage(event.wallpaper)
        is WallpaperDetailsViewModel.Event.OpenUrl -> openUrl(event.url)
    }

    private fun onFocusedWallpaperChanged(focusedWallpaperDestination: WallpaperDestination?) {
        primaryColorTransitionManager.fadeToColor(focusedWallpaperDestination?.colorPaletteModel?.primary, shouldAnimateColorTransitions)
        if (!shouldAnimateColorTransitions) {
            shouldAnimateColorTransitions = true
        }
    }

    private fun setWallpaper(uri: Uri) = context?.setWallpaper(uri)

    private fun showErrorMessage(wallpaper: WallpaperDestination) = context?.let {
        showSnackbar(
            messageResourceId = com.chignonMignon.wallpapers.presentation.shared.R.string.wallpaper_details_cannot_set_wallpaper,
            action = { viewModel.onSetWallpaperButtonPressed(it, wallpaper) }
        )
    }

    private fun openUrl(url: String) = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) // TODO: Improve + add error handling

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