package com.chignonMignon.wallpapers.presentation.feature.collections

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.chignonMignon.wallpapers.presentation.R
import com.chignonMignon.wallpapers.presentation.databinding.FragmentCollectionsBinding
import com.chignonMignon.wallpapers.presentation.feature.Navigator
import com.chignonMignon.wallpapers.presentation.feature.collections.list.CollectionsAdapter
import com.chignonMignon.wallpapers.presentation.utilities.consume
import com.chignonMignon.wallpapers.presentation.utilities.extensions.bind
import com.chignonMignon.wallpapers.presentation.utilities.extensions.colorResource
import com.chignonMignon.wallpapers.presentation.utilities.extensions.navigator
import com.chignonMignon.wallpapers.presentation.utilities.extensions.observe
import com.chignonMignon.wallpapers.presentation.utilities.extensions.showSnackbar
import com.google.android.material.transition.MaterialContainerTransform
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin

class CollectionsFragment : Fragment(R.layout.fragment_collections) {

    private val viewModel by viewModel<CollectionsViewModel>()
    private val collectionsAdapter by lazy {
        CollectionsAdapter(
            onItemSelected = viewModel::onItemSelected
        )
    }
    private var primaryColor: Int? = null
    private var secondaryColor: Int? = null
    private var onSecondaryColor: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
        sharedElementReturnTransition = MaterialContainerTransform()
        postponeEnterTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = bind<FragmentCollectionsBinding>(view)
        binding.viewModel = viewModel
        binding.setupToolbar()
        binding.setupSwipeRefreshLayout()
        binding.setupViewPager()
        viewModel.items.observe(viewLifecycleOwner, collectionsAdapter::submitList)
        viewModel.focusedCollection.observe(viewLifecycleOwner, ::updateColors)
        viewModel.events.observe(viewLifecycleOwner, ::handleEvent)
        (view.parent as? ViewGroup)?.doOnPreDraw { binding.viewPager.post { startPostponedEnterTransition() }}
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

    private fun FragmentCollectionsBinding.setupViewPager() = viewPager.run {
        adapter = collectionsAdapter
        registerOnPageChangeCallback(object : OnPageChangeCallback() {

            override fun onPageSelected(position: Int) = this@CollectionsFragment.viewModel.onPageSelected(position)

            override fun onPageScrollStateChanged(state: Int) {
                swipeRefreshLayout.isEnabled = state == ViewPager2.SCROLL_STATE_IDLE
            }
        })
        setPageTransformer { page, position ->
            page.run {
                val multiplier = 1f - abs(position)
                alpha = multiplier
                scaleX = multiplier
                scaleY = multiplier
                translationX = -width * (position * 0.5f)
                translationY = -height * sin((1 - multiplier) * PI.toFloat()) * 0.05f
            }
        }
    }

    private fun updateColors(collection: Navigator.Collection?) {
        if (primaryColor == null || secondaryColor == null || onSecondaryColor == null) {
            val windowBackgroundColor = context?.colorResource(android.R.attr.windowBackground)
            if (primaryColor == null) {
                primaryColor = windowBackgroundColor
            }
            if (secondaryColor == null) {
                secondaryColor = windowBackgroundColor
            }
            if (onSecondaryColor == null) {
                onSecondaryColor = windowBackgroundColor
            }
        }
        if (collection != null) {
            val newPrimaryColor = collection.colorPalette.primary
            val newSecondaryColor = collection.colorPalette.secondary
            val newOnSecondaryColor = collection.colorPalette.onSecondary
            primaryColor?.let { currentPrimaryColor ->
                secondaryColor?.let { currentSecondaryColor ->
                    onSecondaryColor?.let { currentOnSecondaryColor ->
                        ValueAnimator.ofFloat(0f, 1f).apply {
                            addUpdateListener {
                                viewModel.updateColors(
                                    primaryColor = ColorUtils.blendARGB(currentPrimaryColor, newPrimaryColor, it.animatedFraction),
                                    secondaryColor = ColorUtils.blendARGB(currentSecondaryColor, newSecondaryColor, it.animatedFraction),
                                    onSecondaryColor = ColorUtils.blendARGB(currentOnSecondaryColor, newOnSecondaryColor, it.animatedFraction),
                                )
                            }
                        }.start()
                        primaryColor = newPrimaryColor
                        secondaryColor = newSecondaryColor
                        onSecondaryColor = newOnSecondaryColor
                    }
                }
            }
        }
    }

    private fun handleEvent(event: CollectionsViewModel.Event) = when (event) {
        is CollectionsViewModel.Event.OpenCollectionDetails -> openCollectionDetails(event.collection, event.sharedElements)
        CollectionsViewModel.Event.ShowErrorMessage -> showErrorMessage()
    }

    private fun openCollectionDetails(collection: Navigator.Collection, sharedElements: List<View>) {
        navigator?.navigateToCollectionDetails(collection, sharedElements)
    }

    private fun showErrorMessage() = showSnackbar { viewModel.loadData(true) }

    companion object {
        fun newInstance() = CollectionsFragment()
    }
}