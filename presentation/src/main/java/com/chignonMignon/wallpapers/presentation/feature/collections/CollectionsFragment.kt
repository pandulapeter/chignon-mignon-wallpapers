package com.chignonMignon.wallpapers.presentation.feature.collections

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
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
import com.chignonMignon.wallpapers.presentation.utilities.animateCollectionsAboutButton
import com.chignonMignon.wallpapers.presentation.utilities.animateCollectionsNextButton
import com.chignonMignon.wallpapers.presentation.utilities.animateCollectionsPreviousButton
import com.chignonMignon.wallpapers.presentation.utilities.extensions.autoClearedValue
import com.chignonMignon.wallpapers.presentation.utilities.extensions.bind
import com.chignonMignon.wallpapers.presentation.utilities.extensions.navigator
import com.chignonMignon.wallpapers.presentation.utilities.extensions.observe
import com.chignonMignon.wallpapers.presentation.utilities.extensions.showSnackbar
import com.chignonMignon.wallpapers.presentation.utilities.sharedElementTransition
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class CollectionsFragment : Fragment(R.layout.fragment_collections) {

    private val viewModel by viewModel<CollectionsViewModel>()
    private val collectionsAdapter by lazy {
        CollectionsAdapter(
            onItemSelected = viewModel::onItemSelected,
            onTryAgainButtonClicked = { viewModel.loadData(true) }
        )
    }
    private var binding by autoClearedValue<FragmentCollectionsBinding>()
    private val collectionsColorTransitionManager by lazy {
        CollectionsColorTransitionManager(
            context = requireContext(),
            updateColors = viewModel::updateColors
        )
    }
    private val onBackPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                binding.viewPager.currentItem = 0
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = sharedElementTransition()
        sharedElementReturnTransition = sharedElementTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = bind<FragmentCollectionsBinding>(view).also { binding ->
            binding.viewModel = viewModel
            binding.setupSwipeRefreshLayout()
            binding.setupBackgroundAnimation()
            binding.setupViewPager()
        }
        viewModel.items.observe(viewLifecycleOwner, collectionsAdapter::submitList)
        viewModel.focusedCollection.observe(viewLifecycleOwner, collectionsColorTransitionManager::updateColors)
        viewModel.events.observe(viewLifecycleOwner, ::handleEvent)
        postponeEnterTransition()
        (view.parent as? ViewGroup)?.doOnPreDraw { binding.viewPager.post { startPostponedEnterTransition() } }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    override fun onResume() {
        super.onResume()
        binding.background.alpha = if (viewModel.focusedCollection.value == null) 0f else BACKGROUND_ALPHA
    }

    private fun FragmentCollectionsBinding.setupSwipeRefreshLayout() = swipeRefreshLayout.run {
        setOnRefreshListener { this@CollectionsFragment.viewModel.loadData(true) }
        setColorSchemeResources(R.color.on_primary)
    }

    private fun FragmentCollectionsBinding.setupBackgroundAnimation() = background.run {
        startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_background))
    }

    private fun FragmentCollectionsBinding.setupViewPager() = viewPager.run {
        adapter = collectionsAdapter
        offscreenPageLimit = 1
        registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                this@CollectionsFragment.viewModel.onPageSelected(position)
                onBackPressedCallback.isEnabled = position != 0
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (!this@CollectionsFragment.viewModel.shouldShowLoadingIndicator.value) {
                    swipeRefreshLayout.isEnabled = state == ViewPager2.SCROLL_STATE_IDLE
                }
            }
        })
        setPageTransformer { page, position ->
            when (val pageBinding = page.tag) {
                is ItemCollectionsAboutBinding -> {
                    pageBinding.animate(position)
                    binding.next.run {
                        val adjustedPosition = abs(min(1f, position))
                        binding.next.animateCollectionsNextButton(adjustedPosition)
                        binding.about.animateCollectionsAboutButton(adjustedPosition)
                        binding.background.alpha = adjustedPosition * BACKGROUND_ALPHA
                    }
                }
                is ItemCollectionsCollectionBinding -> pageBinding.animate(position)
                is ItemCollectionsWelcomeBinding -> {
                    pageBinding.animate(position)
                    val adjustedPosition = -max(-1f, position)
                    binding.previous.animateCollectionsPreviousButton(adjustedPosition)
                    binding.background.alpha = adjustedPosition * BACKGROUND_ALPHA
                }
                else -> Unit
            }
        }
    }

    private fun handleEvent(event: CollectionsViewModel.Event) = when (event) {
        is CollectionsViewModel.Event.OpenCollectionDetails -> openCollectionDetails(event.collection, event.sharedElements)
        CollectionsViewModel.Event.NavigateToPreviousPage -> navigateToPreviousPage()
        CollectionsViewModel.Event.NavigateToNextPage -> navigateToNextPage()
        CollectionsViewModel.Event.NavigateToAboutPage -> navigateToAboutPage()
        is CollectionsViewModel.Event.ShowErrorMessage -> showErrorMessage()
        is CollectionsViewModel.Event.ScrollToWelcome -> scrollToWelcome()
    }

    private fun openCollectionDetails(collection: Navigator.Collection, sharedElements: List<View>) {
        navigator?.navigateToCollectionDetails(collection, sharedElements)
    }

    private fun navigateToPreviousPage() {
        binding.viewPager.currentItem--
    }

    private fun navigateToNextPage() {
        binding.viewPager.currentItem++
    }

    private fun navigateToAboutPage() {
        binding.viewPager.currentItem = binding.viewPager.adapter?.run { itemCount - 1 } ?: 0
    }

    private fun showErrorMessage() = context?.let { showSnackbar { viewModel.loadData(true) } }

    private fun scrollToWelcome() {
        binding.viewPager.currentItem = 0
    }

    companion object {
        private const val BACKGROUND_ALPHA = 0.1f

        fun newInstance() = CollectionsFragment()
    }
}