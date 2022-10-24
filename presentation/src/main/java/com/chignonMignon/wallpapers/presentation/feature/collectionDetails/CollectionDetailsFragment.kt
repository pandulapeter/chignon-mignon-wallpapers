package com.chignonMignon.wallpapers.presentation.feature.collectionDetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.chignonMignon.wallpapers.presentation.R
import com.chignonMignon.wallpapers.presentation.databinding.FragmentCollectionDetailsBinding
import com.chignonMignon.wallpapers.presentation.utilities.BundleDelegate
import com.chignonMignon.wallpapers.presentation.utilities.bind
import com.chignonMignon.wallpapers.presentation.utilities.withArguments
import org.koin.androidx.viewmodel.ext.android.viewModel

class CollectionDetailsFragment : Fragment(R.layout.fragment_collection_details) {

    private val viewModel by viewModel<CollectionDetailsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = bind<FragmentCollectionDetailsBinding>(view)
        binding.viewModel = viewModel
    }

    companion object {
        private var Bundle.collectionId by BundleDelegate.String("collectionId")

        fun newInstance(collectionId: String) = CollectionDetailsFragment().withArguments {
            it.collectionId = collectionId
        }
    }
}