package com.chignonMignon.wallpapers.presentation.feature.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.chignonMignon.wallpapers.presentation.R
import com.chignonMignon.wallpapers.presentation.databinding.FragmentCategoriesBinding
import com.chignonMignon.wallpapers.presentation.utilities.bind
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoriesFragment : Fragment(R.layout.fragment_categories) {

    private val viewModel by viewModel<CategoriesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = bind<FragmentCategoriesBinding>(view)
    }

    companion object {
        fun newInstance() = CategoriesFragment()
    }
}