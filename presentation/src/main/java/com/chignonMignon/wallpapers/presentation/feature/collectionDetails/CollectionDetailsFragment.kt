package com.chignonMignon.wallpapers.presentation.feature.collectionDetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.chignonMignon.wallpapers.presentation.R
import com.chignonMignon.wallpapers.presentation.databinding.FragmentCollectionDetailsBinding
import com.chignonMignon.wallpapers.presentation.utilities.BundleDelegate
import com.chignonMignon.wallpapers.presentation.utilities.bind
import com.chignonMignon.wallpapers.presentation.utilities.navigator
import com.chignonMignon.wallpapers.presentation.utilities.observe
import com.chignonMignon.wallpapers.presentation.utilities.withArguments
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CollectionDetailsFragment : Fragment(R.layout.fragment_collection_details) {

    private val viewModel by viewModel<CollectionDetailsViewModel> { parametersOf(arguments?.collectionId) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = bind<FragmentCollectionDetailsBinding>(view)
        binding.viewModel = viewModel
        binding.toolbar.setNavigationOnClickListener { navigator?.navigateBack() }
        viewModel.events.observe(viewLifecycleOwner, ::handleEvent)
        binding.recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            // TODO adapter = collectionsAdapter
        }
        binding.swipeRefreshLayout.setOnRefreshListener { viewModel.loadData(true) }
    }

    private fun handleEvent(event: CollectionDetailsViewModel.Event) = when (event) {
        is CollectionDetailsViewModel.Event.OpenWallpaperDetails -> openWallpaperDetails(event.wallpaperId)
        CollectionDetailsViewModel.Event.ShowErrorMessage -> showErrorMessage()
    }

    private fun openWallpaperDetails(wallpaperId: String) {
        navigator?.navigateToWallpaperDetails(wallpaperId)
    }

    private fun showErrorMessage() {
        // TODO
    }

    companion object {
        private var Bundle.collectionId by BundleDelegate.String("collectionId")

        fun newInstance(collectionId: String) = CollectionDetailsFragment().withArguments {
            it.collectionId = collectionId
        }
    }
}