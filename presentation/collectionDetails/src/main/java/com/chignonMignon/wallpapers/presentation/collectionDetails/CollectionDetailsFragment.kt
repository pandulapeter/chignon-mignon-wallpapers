package com.chignonMignon.wallpapers.presentation.collectionDetails

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.View.OnLayoutChangeListener
import android.view.animation.AnimationUtils
import androidx.core.app.SharedElementCallback
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.chignonMignon.wallpapers.presentation.collectionDetails.databinding.FragmentCollectionDetailsBinding
import com.chignonMignon.wallpapers.presentation.collectionDetails.implementation.CollectionDetailsViewModel
import com.chignonMignon.wallpapers.presentation.collectionDetails.implementation.list.CollectionDetailsAdapter
import com.chignonMignon.wallpapers.presentation.shared.extensions.navigator
import com.chignonMignon.wallpapers.presentation.shared.extensions.showSnackbar
import com.chignonMignon.wallpapers.presentation.shared.navigation.model.CollectionDestination
import com.chignonMignon.wallpapers.presentation.shared.navigation.model.WallpaperDestination
import com.chignonMignon.wallpapers.presentation.utilities.BundleDelegate
import com.chignonMignon.wallpapers.presentation.utilities.extensions.autoClearedValue
import com.chignonMignon.wallpapers.presentation.utilities.extensions.bind
import com.chignonMignon.wallpapers.presentation.utilities.extensions.color
import com.chignonMignon.wallpapers.presentation.utilities.extensions.delaySharedElementTransition
import com.chignonMignon.wallpapers.presentation.utilities.extensions.dimension
import com.chignonMignon.wallpapers.presentation.utilities.extensions.observe
import com.chignonMignon.wallpapers.presentation.utilities.extensions.relativeTranslationX
import com.chignonMignon.wallpapers.presentation.utilities.extensions.scale
import com.chignonMignon.wallpapers.presentation.utilities.extensions.setupTransitions
import com.chignonMignon.wallpapers.presentation.utilities.extensions.withArguments
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class CollectionDetailsFragment : Fragment(R.layout.fragment_collection_details) {

    private val viewModel by viewModel<CollectionDetailsViewModel> { parametersOf(arguments?.collectionDestination) }
    private val collectionDetailsAdapter by lazy {
        CollectionDetailsAdapter(
            onItemSelected = viewModel::onItemSelected
        )
    }
    private var binding by autoClearedValue<FragmentCollectionDetailsBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTransitions()
        setExitSharedElementCallback(
            object : SharedElementCallback() {
                override fun onMapSharedElements(names: List<String?>, sharedElements: MutableMap<String?, View?>) {
                    navigator?.selectedWallpaperIndex?.let { selectedWallpaperIndex ->
                        binding.recyclerView.findViewHolderForAdapterPosition(selectedWallpaperIndex)?.let { selectedViewHolder ->
                            sharedElements[names[0]] = selectedViewHolder.itemView
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
        binding.setupBackgroundAnimation()
        binding.setupSwipeRefreshLayout()
        binding.setupRecyclerView()
        viewModel.items.observe(viewLifecycleOwner, collectionDetailsAdapter::submitList)
        viewModel.events.observe(viewLifecycleOwner, ::handleEvent)
        delaySharedElementTransition(binding.recyclerView)
        viewModel.loadData(false)
    }

    override fun onStart() {
        super.onStart()
        updateActivityWindowBackground()
    }

    override fun onStop() {
        super.onStop()
        updateActivityWindowBackground()
    }

    private fun updateActivityWindowBackground() {
        activity?.window?.decorView?.background = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, viewModel.collection.colorPaletteModel.let { intArrayOf(it.primary, it.secondary) }
        )
    }

    private fun FragmentCollectionDetailsBinding.setupToolbar() {
        toolbar.setNavigationOnClickListener { navigator?.navigateBack() }
        appBarLayout.addOnOffsetChangedListener { _, verticalOffset -> animateHeader(-verticalOffset.toFloat() / appBarLayout.totalScrollRange) }
        collectionBackgroundOverlay.foreground = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, this@CollectionDetailsFragment.viewModel.collection.colorPaletteModel.let {
                intArrayOf(ColorUtils.setAlphaComponent(it.primary, 240), it.secondary)
            }
        )
    }

    private fun FragmentCollectionDetailsBinding.setupBackgroundAnimation() = collectionBackground.run {
        startAnimation(AnimationUtils.loadAnimation(context, com.chignonMignon.wallpapers.presentation.shared.R.anim.anim_background))
    }

    private fun FragmentCollectionDetailsBinding.setupSwipeRefreshLayout() = swipeRefreshLayout.run {
        setOnRefreshListener { this@CollectionDetailsFragment.viewModel.loadData(true) }
        setColorSchemeResources(com.chignonMignon.wallpapers.presentation.shared.R.color.on_primary)
    }

    private fun FragmentCollectionDetailsBinding.setupRecyclerView() = recyclerView.run {
        setHasFixedSize(true)
        val gridLayoutManager = GridLayoutManager(context, getSpanCount())
        layoutManager = gridLayoutManager
        adapter = collectionDetailsAdapter
        navigator?.selectedWallpaperIndex?.let { selectedWallpaperIndex ->
            addOnLayoutChangeListener(
                object : OnLayoutChangeListener {
                    override fun onLayoutChange(view: View, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                        removeOnLayoutChangeListener(this)
                        gridLayoutManager.findViewByPosition(selectedWallpaperIndex)?.let { viewAtPosition ->
                            if (gridLayoutManager.isViewPartiallyVisible(viewAtPosition, false, true)) {
                                post { gridLayoutManager.scrollToPosition(selectedWallpaperIndex) }
                                appBarLayout.setExpanded(false, false)
                            }
                        }
                    }
                }
            )
        }
    }

    private fun getSpanCount(): Int {
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        return displayMetrics.widthPixels / requireContext().dimension(R.dimen.collection_details_wallpaper_item_minimum_width)
    }

    private fun handleEvent(event: CollectionDetailsViewModel.Event) = when (event) {
        CollectionDetailsViewModel.Event.NavigateBack -> navigateBack()
        is CollectionDetailsViewModel.Event.OpenWallpaperDetails -> openWallpaperDetails(event.wallpapers, event.selectedWallpaperIndex, event.sharedElements)
        CollectionDetailsViewModel.Event.ShowErrorMessage -> showErrorMessage()
    }

    private fun navigateBack() {
        navigator?.navigateBack()
    }

    private fun openWallpaperDetails(
        wallpapers: List<WallpaperDestination>, selectedWallpaperIndex: Int, sharedElements: List<View>
    ) {
        navigator?.let {
            it.selectedWallpaperIndex = selectedWallpaperIndex
            it.navigateToWallpaperDetails(wallpapers, selectedWallpaperIndex, sharedElements)
        }
    }

    private fun showErrorMessage() = showSnackbar { viewModel.loadData(true) }

    private fun FragmentCollectionDetailsBinding.animateHeader(multiplier: Float) {
        overlay.alpha = multiplier
        collectionThumbnail.run {
            scaleX = 1f + multiplier * 5f
            scaleY = 1f + multiplier * 3f
            relativeTranslationX(multiplier)
            alpha = 1f - multiplier * multiplier
        }
        description.run {
            alpha = 1f - multiplier * 2f
            pivotX = width.toFloat()
            scale(1f - multiplier)
            relativeTranslationX(multiplier * 0.2f)
        }
        toolbar.setNavigationIconTint(
            ColorUtils.blendARGB(
                this@CollectionDetailsFragment.viewModel.collection.colorPaletteModel.onSecondary,
                toolbar.context.color(com.chignonMignon.wallpapers.presentation.shared.R.color.on_primary),
                multiplier
            )
        )
        divider.run {
            alpha = multiplier
        }
    }

    companion object {
        private var Bundle.collectionDestination by BundleDelegate.Parcelable<CollectionDestination>("collection")

        fun newInstance(collectionDestination: CollectionDestination) = CollectionDetailsFragment().withArguments {
            it.collectionDestination = collectionDestination
        }
    }
}