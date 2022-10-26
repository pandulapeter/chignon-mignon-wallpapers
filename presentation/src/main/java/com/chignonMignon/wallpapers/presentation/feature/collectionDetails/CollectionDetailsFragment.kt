package com.chignonMignon.wallpapers.presentation.feature.collectionDetails

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.graphics.ColorUtils
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.chignonMignon.wallpapers.presentation.R
import com.chignonMignon.wallpapers.presentation.databinding.FragmentCollectionDetailsBinding
import com.chignonMignon.wallpapers.presentation.feature.Navigator
import com.chignonMignon.wallpapers.presentation.feature.collectionDetails.list.CollectionDetailsAdapter
import com.chignonMignon.wallpapers.presentation.utilities.BundleDelegate
import com.chignonMignon.wallpapers.presentation.utilities.extensions.bind
import com.chignonMignon.wallpapers.presentation.utilities.extensions.dimension
import com.chignonMignon.wallpapers.presentation.utilities.extensions.navigator
import com.chignonMignon.wallpapers.presentation.utilities.extensions.observe
import com.chignonMignon.wallpapers.presentation.utilities.extensions.showSnackbar
import com.chignonMignon.wallpapers.presentation.utilities.extensions.withArguments
import com.google.android.material.transition.MaterialContainerTransform
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class CollectionDetailsFragment : Fragment(R.layout.fragment_collection_details) {

    private val viewModel by viewModel<CollectionDetailsViewModel> { parametersOf(arguments?.collection) }
    private val collectionDetailsAdapter by lazy {
        CollectionDetailsAdapter(
            onItemSelected = viewModel::onItemSelected
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply { scrimColor = Color.TRANSPARENT }
        sharedElementReturnTransition = MaterialContainerTransform().apply { scrimColor = Color.TRANSPARENT }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = bind<FragmentCollectionDetailsBinding>(view)
        binding.viewModel = viewModel
        binding.setupToolbar()
        binding.setupBackgroundAnimation()
        binding.setupSwipeRefreshLayout()
        binding.setupRecyclerView()
        viewModel.items.observe(viewLifecycleOwner, collectionDetailsAdapter::submitList)
        viewModel.events.observe(viewLifecycleOwner, ::handleEvent)
        postponeEnterTransition()
        (view.parent as? ViewGroup)?.doOnPreDraw { binding.recyclerView.post { startPostponedEnterTransition() } }
    }

    private fun FragmentCollectionDetailsBinding.setupToolbar() {
        toolbar.setNavigationOnClickListener {
            navigator?.navigateBack()
        }
        appBarLayout.addOnOffsetChangedListener { _, verticalOffset ->
            val multiplier = -verticalOffset.toFloat() / appBarLayout.totalScrollRange
            overlay.alpha = multiplier
            collectionThumbnail.run {
                scaleX = 1f + multiplier * 5f
                scaleY = 1f + multiplier * 3f
                translationX = width * multiplier
                alpha = 1f - multiplier
            }
            description.run {
                alpha = 1f - multiplier * 2f
                pivotX = width.toFloat()
                scaleX = 1f - multiplier
                scaleY = 1f - multiplier
                translationX = width * multiplier * 0.2f
            }
        }
        collectionBackground.foreground = ColorDrawable(ColorUtils.setAlphaComponent(this@CollectionDetailsFragment.viewModel.collection.colorPalette.secondary, 240))
    }

    private fun FragmentCollectionDetailsBinding.setupBackgroundAnimation() = collectionBackground.run {
        startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_background))
    }

    private fun FragmentCollectionDetailsBinding.setupSwipeRefreshLayout() = swipeRefreshLayout.setOnRefreshListener {
        this@CollectionDetailsFragment.viewModel.loadData(true)
    }

    private fun FragmentCollectionDetailsBinding.setupRecyclerView() = recyclerView.run {
        setHasFixedSize(true)
        layoutManager = GridLayoutManager(context, getSpanCount())
        adapter = collectionDetailsAdapter
    }

    private fun getSpanCount(): Int {
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        return displayMetrics.widthPixels / requireContext().dimension(R.dimen.wallpaper_item_minimum_width)
    }

    private fun handleEvent(event: CollectionDetailsViewModel.Event) = when (event) {
        is CollectionDetailsViewModel.Event.OpenWallpaperDetails -> openWallpaperDetails(event.wallpaper)
        CollectionDetailsViewModel.Event.ShowErrorMessage -> showErrorMessage()
    }

    private fun openWallpaperDetails(wallpaper: Navigator.Wallpaper) {
        navigator?.navigateToWallpaperDetails(wallpaper)
    }

    private fun showErrorMessage() = showSnackbar { viewModel.loadData(true) }

    companion object {
        private var Bundle.collection by BundleDelegate.Parcelable<Navigator.Collection>("collection")

        fun newInstance(collection: Navigator.Collection) = CollectionDetailsFragment().withArguments {
            it.collection = collection
        }
    }
}