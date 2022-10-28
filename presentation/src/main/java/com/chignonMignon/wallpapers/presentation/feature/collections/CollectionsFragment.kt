package com.chignonMignon.wallpapers.presentation.feature.collections

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.graphics.ColorUtils
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.chignonMignon.wallpapers.presentation.R
import com.chignonMignon.wallpapers.presentation.databinding.FragmentCollectionsBinding
import com.chignonMignon.wallpapers.presentation.databinding.ItemCollectionsCollectionBinding
import com.chignonMignon.wallpapers.presentation.databinding.ItemCollectionsWelcomeBinding
import com.chignonMignon.wallpapers.presentation.feature.Navigator
import com.chignonMignon.wallpapers.presentation.feature.collections.list.CollectionsAdapter
import com.chignonMignon.wallpapers.presentation.utilities.consume
import com.chignonMignon.wallpapers.presentation.utilities.extensions.bind
import com.chignonMignon.wallpapers.presentation.utilities.extensions.color
import com.chignonMignon.wallpapers.presentation.utilities.extensions.colorResource
import com.chignonMignon.wallpapers.presentation.utilities.extensions.navigator
import com.chignonMignon.wallpapers.presentation.utilities.extensions.observe
import com.chignonMignon.wallpapers.presentation.utilities.extensions.showSnackbar
import com.google.android.material.transition.MaterialContainerTransform
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sin

class CollectionsFragment : Fragment(R.layout.fragment_collections) {

    private val viewModel by viewModel<CollectionsViewModel>()
    private val collectionsAdapter by lazy {
        CollectionsAdapter(
            onItemSelected = viewModel::onItemSelected,
            onTryAgainButtonClicked = { viewModel.loadData(true) }
        )
    }
    private var primaryColor: Int? = null
    private var secondaryColor: Int? = null
    private var binding: FragmentCollectionsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply { scrimColor = Color.TRANSPARENT }
        sharedElementReturnTransition = MaterialContainerTransform().apply { scrimColor = Color.TRANSPARENT }
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
        viewModel.focusedCollection.observe(viewLifecycleOwner, ::updateColors)
        viewModel.events.observe(viewLifecycleOwner, ::handleEvent)
        postponeEnterTransition()
        (view.parent as? ViewGroup)?.doOnPreDraw { binding?.viewPager?.post { startPostponedEnterTransition() } }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
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
        registerOnPageChangeCallback(
            object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) = this@CollectionsFragment.viewModel.onPageSelected(position)

                override fun onPageScrollStateChanged(state: Int) {
                    swipeRefreshLayout.isEnabled = state == ViewPager2.SCROLL_STATE_IDLE
                }
            }
        )
        setPageTransformer { page, position ->
            val multiplier = 1f - abs(position)
            when (val binding = page.tag) {
                is ItemCollectionsCollectionBinding -> {
                    val multiplierSquared = multiplier * multiplier
                    binding.thumbnail.run {
                        alpha = multiplier
                        scaleX = multiplier
                        scaleY = multiplier
                        translationX = -width * (position * 0.5f)
                        translationY = -height * sin((1 - multiplier) * PI.toFloat()) * 0.05f
                    }
                    binding.name.run {
                        alpha = multiplierSquared
                    }
                    binding.description.run {
                        translationX = width * (position * 0.5f)
                        alpha = multiplierSquared
                    }
                }
                is ItemCollectionsWelcomeBinding -> {
                    binding.root.run {
                        alpha = 1f + position * 2f
                        translationX = -width * position
                    }
                    binding.thumbnail.run {
                        val scale = max(0f, 0.6f + position)
                        scaleX = scale
                        scaleY = scale
                    }
                    binding.title.run {
                        translationY = height * position * 4f
                    }
                    binding.message.run {
                        translationY = height * position * 2f
                    }
                }
                else -> Unit
            }
        }
    }

    private fun updateColors(collection: Navigator.Collection?) {
        if (primaryColor == null || secondaryColor == null) {
            val windowBackgroundColor = context?.colorResource(android.R.attr.windowBackground)
            if (primaryColor == null) {
                primaryColor = windowBackgroundColor
            }
            if (secondaryColor == null) {
                secondaryColor = windowBackgroundColor
            }
        }
        val newPrimaryColor = collection?.colorPalette?.primary ?: requireContext().color(R.color.primary)
        val newSecondaryColor = collection?.colorPalette?.secondary ?: requireContext().colorResource(android.R.attr.windowBackground)
        primaryColor?.let { currentPrimaryColor ->
            secondaryColor?.let { currentSecondaryColor ->
                ValueAnimator.ofFloat(0f, 1f).apply {
                    addUpdateListener {
                        viewModel.updateColors(
                            primaryColor = ColorUtils.blendARGB(currentPrimaryColor, newPrimaryColor, it.animatedFraction),
                            secondaryColor = ColorUtils.blendARGB(currentSecondaryColor, newSecondaryColor, it.animatedFraction)
                        )
                    }
                }.start()
                primaryColor = newPrimaryColor
                secondaryColor = newSecondaryColor
            }
        }
    }

    private fun handleEvent(event: CollectionsViewModel.Event) = when (event) {
        is CollectionsViewModel.Event.OpenCollectionDetails -> openCollectionDetails(event.collection, event.sharedElements)
        is CollectionsViewModel.Event.ShowErrorMessage -> showErrorMessage()
        is CollectionsViewModel.Event.ScrollToWelcome -> scrollToWelcome()
    }

    private fun openCollectionDetails(collection: Navigator.Collection, sharedElements: List<View>) {
        navigator?.navigateToCollectionDetails(collection, sharedElements)
    }

    private fun showErrorMessage() = context?.let { showSnackbar { viewModel.loadData(true) } }

    private fun scrollToWelcome() {
        binding?.viewPager?.currentItem = 0
    }

    companion object {
        fun newInstance() = CollectionsFragment()
    }
}