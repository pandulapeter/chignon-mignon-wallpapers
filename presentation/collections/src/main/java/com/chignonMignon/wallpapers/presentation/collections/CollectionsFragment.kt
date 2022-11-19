package com.chignonMignon.wallpapers.presentation.collections

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.chignonMignon.wallpapers.presentation.collections.databinding.FragmentCollectionsBinding
import com.chignonMignon.wallpapers.presentation.collections.databinding.ItemCollectionsAboutBinding
import com.chignonMignon.wallpapers.presentation.collections.databinding.ItemCollectionsCollectionBinding
import com.chignonMignon.wallpapers.presentation.collections.databinding.ItemCollectionsEmptyBinding
import com.chignonMignon.wallpapers.presentation.collections.databinding.ItemCollectionsWelcomeBinding
import com.chignonMignon.wallpapers.presentation.collections.implementation.CollectionsViewModel
import com.chignonMignon.wallpapers.presentation.collections.implementation.animate
import com.chignonMignon.wallpapers.presentation.collections.implementation.animateCollectionsAboutButton
import com.chignonMignon.wallpapers.presentation.collections.implementation.animateCollectionsNextButton
import com.chignonMignon.wallpapers.presentation.collections.implementation.animateCollectionsPreviousButton
import com.chignonMignon.wallpapers.presentation.collections.implementation.list.CollectionsAdapter
import com.chignonMignon.wallpapers.presentation.shared.extensions.navigator
import com.chignonMignon.wallpapers.presentation.shared.extensions.showSnackbar
import com.chignonMignon.wallpapers.presentation.shared.navigation.model.CollectionDestination
import com.chignonMignon.wallpapers.presentation.utilities.ColorTransitionManager
import com.chignonMignon.wallpapers.presentation.utilities.extensions.autoClearedValue
import com.chignonMignon.wallpapers.presentation.utilities.extensions.bind
import com.chignonMignon.wallpapers.presentation.utilities.extensions.color
import com.chignonMignon.wallpapers.presentation.utilities.extensions.colorResource
import com.chignonMignon.wallpapers.presentation.utilities.extensions.delaySharedElementTransition
import com.chignonMignon.wallpapers.presentation.utilities.extensions.observe
import com.chignonMignon.wallpapers.presentation.utilities.extensions.setupTransitions
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class CollectionsFragment : Fragment(R.layout.fragment_collections) {

    private val viewModel by viewModel<CollectionsViewModel>()
    private val collectionsAdapter by lazy {
        CollectionsAdapter(
            onItemSelected = viewModel::onItemSelected,
            onTryAgainButtonClicked = { viewModel.loadData(true, requireContext()) }
        )
    }
    private var binding by autoClearedValue<FragmentCollectionsBinding>()
    private val primaryColorTransitionManager by lazy {
        ColorTransitionManager(requireContext().colorResource(android.R.attr.windowBackground), viewModel::updatePrimaryColor)
    }
    private val secondaryColorTransitionManager by lazy {
        ColorTransitionManager(requireContext().color(com.chignonMignon.wallpapers.presentation.shared.R.color.primary), viewModel::updateSecondaryColor)
    }
    private val onSecondaryColorTransitionManager by lazy {
        ColorTransitionManager(requireContext().color(com.chignonMignon.wallpapers.presentation.shared.R.color.on_primary), viewModel::updateOnSecondaryColor)
    }
    private val onBackPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel.isLastPageFocused.value) {
                    onIsLastPageFocusedChanged(false)
                    onFocusedCollectionChanged(viewModel.focusedCollectionDestination.value)
                }
                binding.viewPager.currentItem = 0
            }
        }
    }
    private var shouldAnimateColorTransitions = false
    private val backgroundGradient by lazy {
        GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            requireContext().colorResource(android.R.attr.windowBackground).let { intArrayOf(it, it) }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTransitions()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = bind(view)
        binding.viewModel = viewModel
        binding.setupRoot()
        binding.setupSwipeRefreshLayout()
        binding.setupBackgroundAnimation()
        binding.setupViewPager()
        shouldAnimateColorTransitions = false
        viewModel.items.observe(viewLifecycleOwner, collectionsAdapter::submitList)
        viewModel.backgroundColor.observe(viewLifecycleOwner, ::updateSwipeRefreshLayoutBackground)
        viewModel.isLastPageFocused.observe(viewLifecycleOwner, ::onIsLastPageFocusedChanged)
        viewModel.focusedCollectionDestination.observe(viewLifecycleOwner, ::onFocusedCollectionChanged)
        viewModel.events.observe(viewLifecycleOwner, ::handleEvent)
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, onBackPressedCallback)
        delaySharedElementTransition(binding.viewPager)
        viewModel.loadData(false, requireContext())
    }

    override fun onResume() {
        super.onResume()
        binding.background.alpha = if (viewModel.focusedCollectionDestination.value == null) 0f else BACKGROUND_ALPHA
        binding.progressBar.finishAnimation()
    }

    private fun FragmentCollectionsBinding.setupRoot() = root.run {
        background = backgroundGradient
    }

    private fun FragmentCollectionsBinding.setupSwipeRefreshLayout() = swipeRefreshLayout.run {
        setOnRefreshListener { this@CollectionsFragment.viewModel.loadData(true, requireContext()) }
        setColorSchemeResources(com.chignonMignon.wallpapers.presentation.shared.R.color.on_primary)
    }

    private fun FragmentCollectionsBinding.setupBackgroundAnimation() = background.run {
        startAnimation(AnimationUtils.loadAnimation(context, com.chignonMignon.wallpapers.presentation.shared.R.anim.anim_background))
    }

    private fun FragmentCollectionsBinding.setupViewPager() = viewPager.run {
        adapter = collectionsAdapter
        offscreenPageLimit = 1
        val viewModel = this@CollectionsFragment.viewModel
        registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.onPageSelected(position)
                onBackPressedCallback.isEnabled = position != 0
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (!viewModel.shouldShowLoadingIndicator.value) {
                    swipeRefreshLayout.isEnabled = state == ViewPager2.SCROLL_STATE_IDLE
                }
            }
        })
        setPageTransformer { page, position ->
            when (val pageBinding = page.tag) {
                is ItemCollectionsAboutBinding -> {
                    pageBinding.animate(position)
                    val adjustedPosition = abs(min(1f, position))
                    binding.next.animateCollectionsNextButton(adjustedPosition)
                    binding.about.animateCollectionsAboutButton(adjustedPosition)
                    binding.background.alpha = adjustedPosition * BACKGROUND_ALPHA
                }
                is ItemCollectionsCollectionBinding -> pageBinding.animate(position)
                is ItemCollectionsEmptyBinding -> {
                    binding.next.animateCollectionsNextButton(abs(min(1f, position)))
                }
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

    private fun updateSwipeRefreshLayoutBackground(colors: Pair<Int?, Int?>) {
        val topColor = colors.first
        val bottomColor = colors.second
        if (topColor != null && bottomColor != null) {
            backgroundGradient.colors = intArrayOf(topColor, bottomColor)
        }
    }

    private fun onIsLastPageFocusedChanged(isLastPageFocused: Boolean) {
        context?.run {
            secondaryColorTransitionManager.defaultColor = if (isLastPageFocused) {
                colorResource(android.R.attr.windowBackground)
            } else {
                color(com.chignonMignon.wallpapers.presentation.shared.R.color.primary)
            }
        }
    }

    private fun onFocusedCollectionChanged(focusedCollectionDestination: CollectionDestination?) {
        primaryColorTransitionManager.fadeToColor(focusedCollectionDestination?.colorPaletteModel?.primary, shouldAnimateColorTransitions)
        secondaryColorTransitionManager.fadeToColor(focusedCollectionDestination?.colorPaletteModel?.secondary, shouldAnimateColorTransitions)
        onSecondaryColorTransitionManager.fadeToColor(focusedCollectionDestination?.colorPaletteModel?.onSecondary, shouldAnimateColorTransitions)
        if (!shouldAnimateColorTransitions) {
            shouldAnimateColorTransitions = true
        }
    }

    private fun handleEvent(event: CollectionsViewModel.Event) = when (event) {
        is CollectionsViewModel.Event.OpenCollectionDetails -> openCollectionDetails(event.collectionDestination, event.sharedElements)
        CollectionsViewModel.Event.NavigateToPreviousPage -> navigateToPreviousPage()
        CollectionsViewModel.Event.NavigateToNextPage -> navigateToNextPage()
        CollectionsViewModel.Event.NavigateToAboutPage -> navigateToAboutPage()
        is CollectionsViewModel.Event.ShowErrorMessage -> showErrorMessage()
        is CollectionsViewModel.Event.ScrollToWelcome -> scrollToWelcome()
    }

    private fun openCollectionDetails(collectionDestination: CollectionDestination, sharedElements: List<View>) {
        navigator?.navigateToCollectionDetails(collectionDestination, sharedElements)
    }

    private fun navigateToPreviousPage() {
        binding.viewPager.currentItem--
    }

    private fun navigateToNextPage() {
        binding.viewPager.currentItem++
    }

    private fun navigateToAboutPage() {
        navigator?.navigateToAbout()
    }

    private fun showErrorMessage() = context?.let { showSnackbar { viewModel.loadData(true, requireContext()) } }

    private fun scrollToWelcome() {
        binding.viewPager.currentItem = 0
    }

    companion object {
        private const val BACKGROUND_ALPHA = 0.1f

        fun newInstance() = CollectionsFragment()
    }
}