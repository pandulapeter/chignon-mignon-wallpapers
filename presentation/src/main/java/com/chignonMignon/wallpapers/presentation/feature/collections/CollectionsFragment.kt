package com.chignonMignon.wallpapers.presentation.feature.collections

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.chignonMignon.wallpapers.presentation.R
import com.chignonMignon.wallpapers.presentation.databinding.FragmentCollectionsBinding
import com.chignonMignon.wallpapers.presentation.databinding.ItemCollectionsAboutBinding
import com.chignonMignon.wallpapers.presentation.databinding.ItemCollectionsCollectionBinding
import com.chignonMignon.wallpapers.presentation.databinding.ItemCollectionsWelcomeBinding
import com.chignonMignon.wallpapers.presentation.feature.Navigator
import com.chignonMignon.wallpapers.presentation.feature.collections.list.CollectionsAdapter
import com.chignonMignon.wallpapers.presentation.utilities.animate
import com.chignonMignon.wallpapers.presentation.utilities.consume
import com.chignonMignon.wallpapers.presentation.utilities.extensions.autoClearedValue
import com.chignonMignon.wallpapers.presentation.utilities.extensions.bind
import com.chignonMignon.wallpapers.presentation.utilities.extensions.navigator
import com.chignonMignon.wallpapers.presentation.utilities.extensions.observe
import com.chignonMignon.wallpapers.presentation.utilities.extensions.showSnackbar
import com.chignonMignon.wallpapers.presentation.utilities.sharedElementTransition
import org.koin.androidx.viewmodel.ext.android.viewModel

class CollectionsFragment : Fragment(R.layout.fragment_collections) {

    private val viewModel by viewModel<CollectionsViewModel>()
    private val collectionsAdapter by lazy {
        CollectionsAdapter(
            onItemSelected = viewModel::onItemSelected,
            onTryAgainButtonClicked = { viewModel.loadData(true) }
        )
    }
    private var binding by autoClearedValue<FragmentCollectionsBinding>()
    private val aboutMenuItem get() = binding.toolbar.menu?.findItem(R.id.about)
    private val collectionsColorTransitionManager by lazy {
        CollectionsColorTransitionManager(
            context = requireContext(),
            updateColors = viewModel::updateColors
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = sharedElementTransition()
        sharedElementReturnTransition = sharedElementTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = bind<FragmentCollectionsBinding>(view).also { binding ->
            binding.viewModel = viewModel
            binding.setupToolbar()
            binding.setupSwipeRefreshLayout()
            binding.setupBackgroundAnimation()
            binding.setupViewPager()
        }
        viewModel.items.observe(viewLifecycleOwner, collectionsAdapter::submitList)
        viewModel.focusedCollection.observe(viewLifecycleOwner, collectionsColorTransitionManager::updateColors)
        viewModel.isAboutIconVisible.observe(viewLifecycleOwner, ::updateAboutIconVisibility)
        viewModel.events.observe(viewLifecycleOwner, ::handleEvent)
        postponeEnterTransition()
        (view.parent as? ViewGroup)?.doOnPreDraw { binding.viewPager.post { startPostponedEnterTransition() } }
    }

    private fun FragmentCollectionsBinding.setupToolbar() = toolbar.setOnMenuItemClickListener { menuItem ->
        consume {
            if (menuItem.itemId == R.id.about) {
                viewPager.currentItem = viewPager.adapter?.run { itemCount - 1 } ?: 0 // TODO
            }
        }
    }

    private fun FragmentCollectionsBinding.setupSwipeRefreshLayout() = swipeRefreshLayout.run {
        setOnRefreshListener { this@CollectionsFragment.viewModel.loadData(true) }
    }

    private fun FragmentCollectionsBinding.setupBackgroundAnimation() = background.run {
        startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_background))
    }

    private fun FragmentCollectionsBinding.setupViewPager() = viewPager.run {
        adapter = collectionsAdapter
        offscreenPageLimit = 1
        registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) = this@CollectionsFragment.viewModel.onPageSelected(position)

            override fun onPageScrollStateChanged(state: Int) {
                if (!this@CollectionsFragment.viewModel.shouldShowLoadingIndicator.value) {
                    swipeRefreshLayout.isEnabled = state == ViewPager2.SCROLL_STATE_IDLE
                }
            }
        })
        setPageTransformer { page, position ->
            when (val binding = page.tag) {
                is ItemCollectionsAboutBinding -> binding.animate(position)
                is ItemCollectionsCollectionBinding -> binding.animate(position)
                is ItemCollectionsWelcomeBinding -> binding.animate(position)
                else -> Unit
            }
        }
    }

    private fun updateAboutIconVisibility(isVisible: Boolean) {
        aboutMenuItem?.isVisible = isVisible
    }

    private fun handleEvent(event: CollectionsViewModel.Event) = when (event) {
        is CollectionsViewModel.Event.OpenCollectionDetails -> openCollectionDetails(event.collection, event.sharedElements)
        CollectionsViewModel.Event.NavigateToPreviousPage -> navigateToPreviousPage()
        CollectionsViewModel.Event.NavigateToNextPage -> navigateToNextPage()
        is CollectionsViewModel.Event.ShowErrorMessage -> showErrorMessage()
        is CollectionsViewModel.Event.ScrollToWelcome -> scrollToWelcome()
    }

    private fun openCollectionDetails(collection: Navigator.Collection, sharedElements: List<View>) {
        navigator?.navigateToCollectionDetails(collection, sharedElements)
    }

    private fun navigateToNextPage() {
        binding.viewPager.currentItem++
    }

    private fun navigateToPreviousPage() {
        binding.viewPager.currentItem--
    }

    private fun showErrorMessage() = context?.let { showSnackbar { viewModel.loadData(true) } }

    private fun scrollToWelcome() {
        binding.viewPager.currentItem = 0
    }

    companion object {
        fun newInstance() = CollectionsFragment()
    }
}