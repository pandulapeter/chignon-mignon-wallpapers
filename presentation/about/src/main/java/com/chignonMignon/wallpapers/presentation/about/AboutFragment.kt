package com.chignonMignon.wallpapers.presentation.about

import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.chignonMignon.wallpapers.presentation.about.databinding.FragmentAboutBinding
import com.chignonMignon.wallpapers.presentation.about.implementation.AboutViewModel
import com.chignonMignon.wallpapers.presentation.shared.extensions.navigator
import com.chignonMignon.wallpapers.presentation.utilities.extensions.bind
import com.chignonMignon.wallpapers.presentation.utilities.extensions.colorResource
import com.chignonMignon.wallpapers.presentation.utilities.extensions.setupTransitions
import org.koin.androidx.viewmodel.ext.android.viewModel

class AboutFragment : Fragment(R.layout.fragment_about) {

    private val viewModel by viewModel<AboutViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTransitions()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = bind<FragmentAboutBinding>(view)
        binding.viewModel = viewModel
        binding.setupToolbar()
        activity?.window?.decorView?.background = AppCompatResources.getDrawable(requireContext(), R.drawable.bg_about)
    }

    private fun FragmentAboutBinding.setupToolbar() = toolbar.setNavigationOnClickListener {
        navigator?.navigateBack()
    }

    companion object {
        fun newInstance() = AboutFragment()
    }
}