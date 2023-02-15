package com.chignonMignon.wallpapers.presentation.wallpaperDetails

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.annotation.StringRes
import androidx.core.app.SharedElementCallback
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.chignonMignon.wallpapers.presentation.shared.extensions.navigator
import com.chignonMignon.wallpapers.presentation.shared.extensions.openUrl
import com.chignonMignon.wallpapers.presentation.shared.extensions.showSnackbar
import com.chignonMignon.wallpapers.presentation.shared.navigation.model.WallpaperDestination
import com.chignonMignon.wallpapers.presentation.utilities.BundleDelegate
import com.chignonMignon.wallpapers.presentation.utilities.ColorTransitionManager
import com.chignonMignon.wallpapers.presentation.utilities.extensions.autoClearedValue
import com.chignonMignon.wallpapers.presentation.utilities.extensions.bind
import com.chignonMignon.wallpapers.presentation.utilities.extensions.color
import com.chignonMignon.wallpapers.presentation.utilities.extensions.colorResource
import com.chignonMignon.wallpapers.presentation.utilities.extensions.delaySharedElementTransition
import com.chignonMignon.wallpapers.presentation.utilities.extensions.observe
import com.chignonMignon.wallpapers.presentation.utilities.extensions.setupTransitions
import com.chignonMignon.wallpapers.presentation.utilities.extensions.withArguments
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.databinding.FragmentWallpaperDetailsBinding
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.WallpaperDetailsViewModel
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.productList.WallpaperDetailsProductAdapter
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.wallpaperList.OnWallpaperTypeSelectedListener
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.wallpaperList.WallpaperDetailsAdapter
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.wallpaperList.WallpaperType
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.wallpaperList.WallpaperTypeSelectorBottomSheetFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class WallpaperDetailsFragment : Fragment(R.layout.fragment_wallpaper_details), OnWallpaperTypeSelectedListener {

    private val viewModel by viewModel<WallpaperDetailsViewModel> {
        parametersOf(arguments?.wallpapers, arguments?.selectedWallpaperIndex)
    }
    private val wallpaperDetailsAdapter by lazy { WallpaperDetailsAdapter() }
    private var binding by autoClearedValue<FragmentWallpaperDetailsBinding>()
    private val primaryColorTransitionManager by lazy {
        ColorTransitionManager(requireContext().color(com.chignonMignon.wallpapers.presentation.shared.R.color.brand_yellow), viewModel::updatePrimaryColor)
    }
    private val secondaryColorTransitionManager by lazy {
        ColorTransitionManager(requireContext().colorResource(android.R.attr.windowBackground), viewModel::updateSecondaryColor)
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
        binding.setupFloatingActionButton()
        viewModel.focusedWallpaper.observe(viewLifecycleOwner, ::onFocusedWallpaperChanged)
        viewModel.productListItems.observe(viewLifecycleOwner, productAdapter::submitList)
        viewModel.events.observe(viewLifecycleOwner, ::handleEvent)
        delaySharedElementTransition(binding.content.viewPager)
    }

    override fun onResume() {
        super.onResume()
        binding.appBar.progressBar.finishAnimation()
    }

    override fun onStop() {
        super.onStop()
        navigator?.selectedWallpaperIndex = binding.content.viewPager.currentItem
    }

    private fun FragmentWallpaperDetailsBinding.setupToolbar() = appBar.toolbar.run {
        setOnClickListener { navigator?.navigateBack() }
        setNavigationOnClickListener { navigator?.navigateBack() }
    }

    private fun FragmentWallpaperDetailsBinding.setupViewPager() = content.viewPager.run {
        val viewModel = this@WallpaperDetailsFragment.viewModel
        wallpaperDetailsAdapter.submitList(viewModel.wallpaperListItems)
        adapter = wallpaperDetailsAdapter
        offscreenPageLimit = 1
        setCurrentItem(getCurrentVisibleItemIndex(), false)
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

    private fun FragmentWallpaperDetailsBinding.setupFloatingActionButton() = floatingActionButton.run {
        setOnClickListener {
            WallpaperTypeSelectorBottomSheetFragment.show(
                fragmentManager = childFragmentManager,
                backgroundColor = this@WallpaperDetailsFragment.viewModel.focusedWallpaper.value.colorPaletteModel.primary
            )
        }
        startAnimation(AnimationUtils.loadAnimation(binding.root.context, com.chignonMignon.wallpapers.presentation.shared.R.anim.anim_pulsate))
    }

    override fun onHomeScreenSelected() = setWallpaper(WallpaperType.HOME_SCREEN)

    override fun onLockScreenSelected() = setWallpaper(WallpaperType.LOCK_SCREEN)

    override fun onBothSelected() = setWallpaper(WallpaperType.BOTH)

    private fun setWallpaper(wallpaperType: WallpaperType) {
        getCurrentBitmap()?.let { bitmap ->
            getCurrentCropRect()?.let { cropRect ->
                context?.run { viewModel.onSetWallpaperButtonPressed(this, bitmap, cropRect, wallpaperType) }
            }
        }
    }

    private fun onFocusedWallpaperChanged(focusedWallpaperDestination: WallpaperDestination?) {
        primaryColorTransitionManager.fadeToColor(focusedWallpaperDestination?.colorPaletteModel?.primary, shouldAnimateColorTransitions)
        secondaryColorTransitionManager.fadeToColor(focusedWallpaperDestination?.colorPaletteModel?.secondary, shouldAnimateColorTransitions)
        if (!shouldAnimateColorTransitions) {
            shouldAnimateColorTransitions = true
        }
    }

    private fun getViewPagerRecyclerView() = (binding.content.viewPager[0] as RecyclerView)

    private fun getCurrentVisibleItemIndex() = viewModel.wallpaperListItems.indexOfFirst { it.wallpaper.id == viewModel.focusedWallpaper.value.id }

    private fun getCurrentViewHolder() =
        getViewPagerRecyclerView().findViewHolderForAdapterPosition(getCurrentVisibleItemIndex())

    private fun getCurrentBitmap() = (getCurrentViewHolder()?.itemView?.tag as? WallpaperDetailsAdapter.GetCropRectCallback)?.getBitmap()

    private fun getCurrentCropRect() = (getCurrentViewHolder()?.itemView?.tag as? WallpaperDetailsAdapter.GetCropRectCallback)?.getCropRect()

    private fun handleEvent(event: WallpaperDetailsViewModel.Event) = when (event) {
        WallpaperDetailsViewModel.Event.WallpaperSet -> showMessage(com.chignonMignon.wallpapers.presentation.shared.R.string.wallpaper_details_wallpaper_applied_successfully)
        WallpaperDetailsViewModel.Event.WallpaperNotSet -> showMessage(com.chignonMignon.wallpapers.presentation.shared.R.string.wallpaper_details_cannot_set_wallpaper_apply)
        is WallpaperDetailsViewModel.Event.OpenUrl -> openUrl(event.url)
    }

    private fun showMessage(@StringRes messageResourceId: Int) = context?.let {
        showSnackbar(
            anchor = binding.coordinatorLayout,
            messageResourceId = messageResourceId,
        )
    }

    private fun openUrl(url: String) = context?.openUrl(url, binding.coordinatorLayout)

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