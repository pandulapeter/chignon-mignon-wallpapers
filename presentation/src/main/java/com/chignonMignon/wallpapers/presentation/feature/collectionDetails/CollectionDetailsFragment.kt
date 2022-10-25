package com.chignonMignon.wallpapers.presentation.feature.collectionDetails

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.chignonMignon.wallpapers.presentation.R
import com.chignonMignon.wallpapers.presentation.databinding.FragmentCollectionDetailsBinding
import com.chignonMignon.wallpapers.presentation.feature.Navigator
import com.chignonMignon.wallpapers.presentation.feature.collectionDetails.list.CollectionDetailsAdapter
import com.chignonMignon.wallpapers.presentation.utilities.BundleDelegate
import com.chignonMignon.wallpapers.presentation.utilities.bind
import com.chignonMignon.wallpapers.presentation.utilities.dimension
import com.chignonMignon.wallpapers.presentation.utilities.navigator
import com.chignonMignon.wallpapers.presentation.utilities.observe
import com.chignonMignon.wallpapers.presentation.utilities.withArguments
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CollectionDetailsFragment : Fragment(R.layout.fragment_collection_details) {

    private val viewModel by viewModel<CollectionDetailsViewModel> { parametersOf(arguments?.collection) }
    private val collectionDetailsAdapter by lazy {
        CollectionDetailsAdapter(
            onItemSelected = viewModel::onItemSelected
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = bind<FragmentCollectionDetailsBinding>(view)
        binding.viewModel = viewModel
        binding.setupToolbar()
        binding.setupSwipeRefreshLayout()
        binding.setupRecyclerView()
        viewModel.items.observe(viewLifecycleOwner, collectionDetailsAdapter::submitList)
        viewModel.events.observe(viewLifecycleOwner, ::handleEvent)
    }

    private fun FragmentCollectionDetailsBinding.setupToolbar() = toolbar.setNavigationOnClickListener {
        navigator?.navigateBack()
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

    private fun showErrorMessage() {
        // TODO
    }

    companion object {
        private var Bundle.collection by BundleDelegate.Parcelable<Navigator.Collection>("collection")

        fun newInstance(collection: Navigator.Collection) = CollectionDetailsFragment().withArguments {
            it.collection = collection
        }
    }
}