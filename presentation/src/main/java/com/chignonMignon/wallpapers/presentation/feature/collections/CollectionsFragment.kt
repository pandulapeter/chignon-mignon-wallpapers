package com.chignonMignon.wallpapers.presentation.feature.collections

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.chignonMignon.wallpapers.presentation.R
import com.chignonMignon.wallpapers.presentation.databinding.FragmentCollectionsBinding
import com.chignonMignon.wallpapers.presentation.utilities.bind
import org.koin.androidx.viewmodel.ext.android.viewModel

class CollectionsFragment : Fragment(R.layout.fragment_collections) {

    private val viewModel by viewModel<CollectionsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = bind<FragmentCollectionsBinding>(view)
        binding.viewModel = viewModel
    }

    companion object {
        fun newInstance() = CollectionsFragment()
    }
}