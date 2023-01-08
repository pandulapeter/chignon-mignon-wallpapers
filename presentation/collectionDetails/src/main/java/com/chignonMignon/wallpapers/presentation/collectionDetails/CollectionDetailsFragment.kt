package com.chignonMignon.wallpapers.presentation.collectionDetails

import android.animation.LayoutTransition
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
import com.chignonMignon.wallpapers.presentation.utilities.extensions.ImageViewTag
import com.chignonMignon.wallpapers.presentation.utilities.extensions.autoClearedValue
import com.chignonMignon.wallpapers.presentation.utilities.extensions.bind
import com.chignonMignon.wallpapers.presentation.utilities.extensions.dimension
import com.chignonMignon.wallpapers.presentation.utilities.extensions.imageViewTag
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
    private val backgroundGradient by lazy {
        GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(viewModel.collection.colorPaletteModel.secondary))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTransitions()
        setExitSharedElementCallback(
            object : SharedElementCallback() {
                override fun onMapSharedElements(names: List<String?>, sharedElements: MutableMap<String?, View?>) {
                    navigator?.selectedWallpaperIndex?.let { selectedWallpaperIndex ->
                        binding.recyclerView.run {
                            findViewHolderForAdapterPosition(selectedWallpaperIndex)?.itemView?.let { sharedElementView ->
                                if (names.isEmpty() || sharedElementView.transitionName == null) {
                                    sharedElements.clear()
                                } else {
                                    sharedElements[names[0]] = sharedElementView
                                }
                            }
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
        viewModel.loadData(false)
        postponeEnterTransition()
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
        collapsingToolbarLayout.setOnClickListener { navigator?.navigateBack() }
        toolbar.setOnClickListener { navigator?.navigateBack() }
        toolbar.setNavigationOnClickListener { navigator?.navigateBack() }
        appBarLayout.addOnOffsetChangedListener { _, verticalOffset -> animateHeader(-verticalOffset.toFloat() / appBarLayout.totalScrollRange) }
        collectionBackgroundOverlay.foreground = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, this@CollectionDetailsFragment.viewModel.collection.colorPaletteModel.let {
                intArrayOf(ColorUtils.setAlphaComponent(it.primary, 240), it.secondary)
            }
        )
        collectionThumbnail.layoutTransition = LayoutTransition().apply {
            setAnimateParentHierarchy(false)
        }
        collectionThumbnailImage.run {
            tag = imageViewTag?.copy(loadingIndicator = binding.loadingIndicator) ?: ImageViewTag(loadingIndicator = binding.loadingIndicator)
        }
    }

    private fun FragmentCollectionDetailsBinding.setupBackgroundAnimation() = collectionBackground.run {
        startAnimation(AnimationUtils.loadAnimation(context, com.chignonMignon.wallpapers.presentation.shared.R.anim.anim_background))
    }

    private fun FragmentCollectionDetailsBinding.setupSwipeRefreshLayout() = swipeRefreshLayout.run {
        background = backgroundGradient
        setOnRefreshListener { this@CollectionDetailsFragment.viewModel.loadData(true) }
        setColorSchemeResources(com.chignonMignon.wallpapers.presentation.shared.R.color.brand_text)
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
                        gridLayoutManager.scrollToPositionWithOffset(selectedWallpaperIndex, context.dimension(R.dimen.collection_details_wallpaper_item_minimum_width))
                        post {
                            if (computeVerticalScrollOffset() > 0) {
                                binding.appBarLayout.setExpanded(false, false)
                            }
                            startPostponedEnterTransition()
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
        val viewModel = this@CollectionDetailsFragment.viewModel
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
        divider.run {
            alpha = multiplier
        }
        swipeRefreshLayout.background = (backgroundGradient.mutate() as GradientDrawable).apply {
            viewModel.collection.colorPaletteModel.let {
                colors = intArrayOf(ColorUtils.blendARGB(it.primary, it.secondary, 1f - multiplier), it.secondary)
            }
        }
    }

    companion object {
        private var Bundle.collectionDestination by BundleDelegate.Parcelable<CollectionDestination>("collection")

        fun newInstance(collectionDestination: CollectionDestination) = CollectionDetailsFragment().withArguments {
            it.collectionDestination = collectionDestination
        }
    }
}