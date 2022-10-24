package com.chignonMignon.wallpapers.presentation.feature.collections

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.chignonMignon.wallpapers.presentation.R
import com.chignonMignon.wallpapers.presentation.databinding.FragmentCollectionsBinding
import com.chignonMignon.wallpapers.presentation.feature.collections.list.CollectionsAdapter
import com.chignonMignon.wallpapers.presentation.utilities.bind
import com.chignonMignon.wallpapers.presentation.utilities.consume
import com.chignonMignon.wallpapers.presentation.utilities.navigator
import com.chignonMignon.wallpapers.presentation.utilities.observe
import org.koin.androidx.viewmodel.ext.android.viewModel

class CollectionsFragment : Fragment(R.layout.fragment_collections) {

    private val viewModel by viewModel<CollectionsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = bind<FragmentCollectionsBinding>(view)
        binding.viewModel = viewModel
        val collectionsAdapter = CollectionsAdapter(
            onItemSelected = viewModel::onItemSelected
        )
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            consume {
                if (menuItem.itemId == R.id.about) {
                    navigator?.navigateToAbout()
                }
            }
        }
        viewModel.items.observe(viewLifecycleOwner, collectionsAdapter::submitList)
        binding.recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = collectionsAdapter
        }
        binding.swipeRefreshLayout.setOnRefreshListener { viewModel.loadData(true) }
    }

    companion object {
        fun newInstance() = CollectionsFragment()
    }
}