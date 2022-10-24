package com.chignonMignon.wallpapers.presentation.feature.about

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.chignonMignon.wallpapers.presentation.R
import com.chignonMignon.wallpapers.presentation.databinding.FragmentAboutBinding
import com.chignonMignon.wallpapers.presentation.utilities.bind
import com.chignonMignon.wallpapers.presentation.utilities.navigator
import org.koin.androidx.viewmodel.ext.android.viewModel

class AboutFragment : Fragment(R.layout.fragment_about) {

    private val viewModel by viewModel<AboutViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = bind<FragmentAboutBinding>(view)
        binding.viewModel = viewModel
        binding.toolbar.setNavigationOnClickListener { navigator?.navigateBack() }
    }

    companion object {
        fun newInstance() = AboutFragment()
    }
}