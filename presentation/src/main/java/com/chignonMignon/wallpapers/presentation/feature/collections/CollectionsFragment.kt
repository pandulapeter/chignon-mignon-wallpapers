package com.chignonMignon.wallpapers.presentation.feature.collections

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.chignonMignon.wallpapers.presentation.R
import com.chignonMignon.wallpapers.presentation.databinding.FragmentCollectionsBinding
import com.chignonMignon.wallpapers.presentation.feature.Navigator
import com.chignonMignon.wallpapers.presentation.feature.collections.list.CollectionsAdapter
import com.chignonMignon.wallpapers.presentation.utilities.bind
import com.chignonMignon.wallpapers.presentation.utilities.consume
import com.chignonMignon.wallpapers.presentation.utilities.navigator
import com.chignonMignon.wallpapers.presentation.utilities.observe
import com.chignonMignon.wallpapers.presentation.utilities.showSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class CollectionsFragment : Fragment(R.layout.fragment_collections) {

    private val viewModel by viewModel<CollectionsViewModel>()
    private val collectionsAdapter by lazy {
        CollectionsAdapter(
            onItemSelected = viewModel::onItemSelected
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = bind<FragmentCollectionsBinding>(view)
        binding.viewModel = viewModel
        binding.setupToolbar()
        binding.setupSwipeRefreshLayout()
        binding.setupRecyclerView()
        viewModel.items.observe(viewLifecycleOwner, collectionsAdapter::submitList)
        viewModel.events.observe(viewLifecycleOwner, ::handleEvent)
    }

    private fun FragmentCollectionsBinding.setupToolbar() = toolbar.setOnMenuItemClickListener { menuItem ->
        consume {
            if (menuItem.itemId == R.id.about) {
                navigator?.navigateToAbout()
            }
        }
    }

    private fun FragmentCollectionsBinding.setupSwipeRefreshLayout() = swipeRefreshLayout.setOnRefreshListener {
        this@CollectionsFragment.viewModel.loadData(true)
    }

    private fun FragmentCollectionsBinding.setupRecyclerView() = recyclerView.run {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        adapter = collectionsAdapter
    }

    private fun handleEvent(event: CollectionsViewModel.Event) = when (event) {
        is CollectionsViewModel.Event.OpenCollectionDetails -> openCollectionDetails(event.collection)
        CollectionsViewModel.Event.ShowErrorMessage -> showErrorMessage()
    }

    private fun openCollectionDetails(collection: Navigator.Collection) {
        navigator?.navigateToCollectionDetails(collection)
    }

    private fun showErrorMessage() = showSnackbar { viewModel.loadData(true) }

    companion object {
        fun newInstance() = CollectionsFragment()
    }
}