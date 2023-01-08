package com.chignonMignon.wallpapers.presentation.collections

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import androidx.activity.OnBackPressedCallback
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
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
import com.chignonMignon.wallpapers.presentation.collections.implementation.animateCollectionsNextButtonHint
import com.chignonMignon.wallpapers.presentation.collections.implementation.animateCollectionsPreviousButton
import com.chignonMignon.wallpapers.presentation.collections.implementation.list.CollectionsAdapter
import com.chignonMignon.wallpapers.presentation.shared.extensions.navigator
import com.chignonMignon.wallpapers.presentation.shared.extensions.showSnackbar
import com.chignonMignon.wallpapers.presentation.shared.navigation.model.CollectionDestination
import com.chignonMignon.wallpapers.presentation.utilities.BundleDelegate
import com.chignonMignon.wallpapers.presentation.utilities.ColorTransitionManager
import com.chignonMignon.wallpapers.presentation.utilities.TRANSITION_DURATION
import com.chignonMignon.wallpapers.presentation.utilities.extensions.autoClearedValue
import com.chignonMignon.wallpapers.presentation.utilities.extensions.bind
import com.chignonMignon.wallpapers.presentation.utilities.extensions.color
import com.chignonMignon.wallpapers.presentation.utilities.extensions.colorResource
import com.chignonMignon.wallpapers.presentation.utilities.extensions.delaySharedElementTransition
import com.chignonMignon.wallpapers.presentation.utilities.extensions.observe
import com.chignonMignon.wallpapers.presentation.utilities.extensions.setupTransitions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class CollectionsFragment : Fragment(R.layout.fragment_collections) {

    private val viewModel by viewModel<CollectionsViewModel>()
    private val collectionsAdapter by lazy {
        CollectionsAdapter(
            onPreviousPageNavigationHelperClicked = ::navigateToPreviousPage,
            onNextPageNavigationHelperClicked = ::navigateToNextPage,
            onItemSelected = viewModel::onItemSelected,
            onTryAgainButtonClicked = { viewModel.loadData(true) }
        )
    }
    private var binding by autoClearedValue<FragmentCollectionsBinding>()
    private val primaryColorTransitionManager by lazy {
        ColorTransitionManager(requireContext().colorResource(android.R.attr.windowBackground), viewModel::updatePrimaryColor)
    }
    private val secondaryColorTransitionManager by lazy {
        ColorTransitionManager(requireContext().color(com.chignonMignon.wallpapers.presentation.shared.R.color.brand_yellow), viewModel::updateSecondaryColor)
    }
    private val onBackPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToPreviousPage()
            }
        }
    }
    private var shouldAnimateColorTransitions = false
    private val backgroundGradient by lazy {
        GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, requireContext().colorResource(android.R.attr.windowBackground).let { intArrayOf(it, it) })
    }
    private var currentItem: Int? = null
    private var isInBetweenPages = false

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
        binding.setupHintView()
        shouldAnimateColorTransitions = false
        viewModel.items.observe(viewLifecycleOwner, collectionsAdapter::submitList)
        viewModel.backgroundColor.observe(viewLifecycleOwner, ::updateSwipeRefreshLayoutBackground)
        viewModel.isLastPageFocused.observe(viewLifecycleOwner, ::onIsLastPageFocusedChanged)
        viewModel.focusedCollectionDestination.observe(viewLifecycleOwner, ::onFocusedCollectionChanged)
        viewModel.events.observe(viewLifecycleOwner, ::handleEvent)
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, onBackPressedCallback)
        delaySharedElementTransition(binding.viewPager)
        viewModel.loadData(false)
        currentItem = currentItem ?: savedInstanceState?.currentItem
    }

    override fun onResume() {
        super.onResume()
        fun updateUi() {
            binding.background.alpha = if (viewModel.focusedCollectionDestination.value == null) 0f else BACKGROUND_ALPHA
            binding.progressBar.finishAnimation()
        }
        currentItem?.let { currentItem ->
            binding.viewPager.doOnPreDraw {
                binding.viewPager.setCurrentItem(currentItem, false)
                onPageSelected(currentItem)
                onFocusedCollectionChanged(viewModel.focusedCollectionDestination.value)
                updateUi()
            }
        }
        updateUi()
    }

    override fun startPostponedEnterTransition() {
        super.startPostponedEnterTransition()
        viewLifecycleOwner.lifecycleScope.launch {
            delay(TRANSITION_DURATION)
            binding.viewPager.isUserInputEnabled = true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        currentItem?.let { currentItem ->
            outState.currentItem = currentItem
        }
    }

    private fun FragmentCollectionsBinding.setupRoot() = root.run {
        background = backgroundGradient
    }

    private fun FragmentCollectionsBinding.setupSwipeRefreshLayout() = swipeRefreshLayout.run {
        setOnRefreshListener { this@CollectionsFragment.viewModel.loadData(true) }
        setColorSchemeResources(com.chignonMignon.wallpapers.presentation.shared.R.color.brand_text)
    }

    private fun FragmentCollectionsBinding.setupBackgroundAnimation() = background.run {
        startAnimation(AnimationUtils.loadAnimation(context, com.chignonMignon.wallpapers.presentation.shared.R.anim.anim_background))
    }

    private fun FragmentCollectionsBinding.setupViewPager() = viewPager.run {
        adapter = collectionsAdapter
        val viewModel = this@CollectionsFragment.viewModel
        registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) = this@CollectionsFragment.onPageSelected(position)

            override fun onPageScrollStateChanged(state: Int) {
                if (!viewModel.shouldShowLoadingIndicator.value) {
                    swipeRefreshLayout.isEnabled = state == ViewPager2.SCROLL_STATE_IDLE
                }
            }
        })
        setPageTransformer { page, position ->
            isInBetweenPages = position != position.roundToInt().toFloat()
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
                    binding.hintView.animateCollectionsNextButtonHint(adjustedPosition)
                    binding.background.alpha = adjustedPosition * BACKGROUND_ALPHA
                }
                else -> Unit
            }
        }
        post { isUserInputEnabled = false }
    }

    private val nextUpscaleAnimation: Animation by lazy { createNextAnimation(1f, NEXT_ANIMATION_SCALE_X) { nextDownscaleAnimation } }
    private val nextDownscaleAnimation: Animation by lazy {
        createNextAnimation(NEXT_ANIMATION_SCALE_X, 1f) {
            if (binding.viewPager.currentItem == 0) nextUpscaleAnimation else nextHoldAnimation
        }
    }
    private val nextHoldAnimation: Animation by lazy {
        createNextAnimation(1f, 1f) {
            if (binding.viewPager.currentItem == 0) nextUpscaleAnimation else nextHoldAnimation
        }
    }

    private fun FragmentCollectionsBinding.setupHintView() = hintView.run {
        targetView = next
        next.startAnimation(nextHoldAnimation)
    }

    private fun createNextAnimation(
        fromX: Float,
        toX: Float,
        nextAnimation: () -> Animation
    ) = ScaleAnimation(fromX, toX, 1f, 1f, 0.5f, 0.5f).apply {
        setAnimationListener(
            object : AnimationListener {
                override fun onAnimationStart(animation: Animation?) = Unit

                override fun onAnimationEnd(animation: Animation?) = binding.next.startAnimation(nextAnimation())

                override fun onAnimationRepeat(animation: Animation?) = Unit
            }
        )
        duration = 250L
    }

    private fun onPageSelected(position: Int) {
        currentItem = position
        if (position == collectionsAdapter.itemCount - 1) {
            binding.progressBar.run {
                progress = 1f
                finishAnimation()
            }
        }
        viewModel.onPageSelected(position)
        onBackPressedCallback.isEnabled = position != 0
        binding.hintView.radiusMultiplier = if (position == 0) 1f else 0f
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
            val primaryColor = color(com.chignonMignon.wallpapers.presentation.shared.R.color.brand_yellow)
            val windowBackgroundColor = colorResource(android.R.attr.windowBackground)
            primaryColorTransitionManager.defaultColor = if (isLastPageFocused) primaryColor else windowBackgroundColor
            secondaryColorTransitionManager.defaultColor = if (isLastPageFocused) windowBackgroundColor else primaryColor
        }
    }

    private fun onFocusedCollectionChanged(focusedCollectionDestination: CollectionDestination?) {
        primaryColorTransitionManager.fadeToColor(focusedCollectionDestination?.colorPaletteModel?.primary, shouldAnimateColorTransitions)
        secondaryColorTransitionManager.fadeToColor(focusedCollectionDestination?.colorPaletteModel?.secondary, shouldAnimateColorTransitions)
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
        if (!isInBetweenPages) {
            navigator?.navigateToCollectionDetails(collectionDestination, sharedElements)
        }
    }

    private fun navigateToPreviousPage() = binding.viewPager.run {
        if (!isInBetweenPages && isUserInputEnabled) {
            currentItem--
        }
    }

    private fun navigateToNextPage() = binding.viewPager.run {
        if (!isInBetweenPages && isUserInputEnabled) {
            currentItem++
        }
    }

    private fun navigateToAboutPage() {
        navigator?.navigateToAbout()
    }

    private fun showErrorMessage() = showSnackbar(anchor = binding.coordinatorLayout) {
        viewModel.loadData(true)
    }

    private fun scrollToWelcome() {
        binding.viewPager.currentItem = 0
    }

    companion object {
        private const val NEXT_ANIMATION_SCALE_X = 1.1f
        private const val BACKGROUND_ALPHA = 0.1f
        private var Bundle.currentItem by BundleDelegate.Int("currentItem")

        fun newInstance() = CollectionsFragment()
    }
}