package com.chignonMignon.wallpapers.presentation.feature.categoryDetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.chignonMignon.wallpapers.presentation.R
import com.chignonMignon.wallpapers.presentation.databinding.FragmentCategoryDetailsBinding
import com.chignonMignon.wallpapers.presentation.utilities.BundleDelegate
import com.chignonMignon.wallpapers.presentation.utilities.bind
import com.chignonMignon.wallpapers.presentation.utilities.withArguments
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoryDetailsFragment : Fragment(R.layout.fragment_category_details) {

    private val viewModel by viewModel<CategoryDetailsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = bind<FragmentCategoryDetailsBinding>(view)
    }

    companion object {
        private var Bundle.categoryId by BundleDelegate.String("categoryId")

        fun newInstance(categoryId: String) = CategoryDetailsFragment().withArguments {
            it.categoryId = categoryId
        }
    }
}