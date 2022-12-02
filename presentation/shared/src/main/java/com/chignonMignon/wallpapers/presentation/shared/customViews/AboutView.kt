package com.chignonMignon.wallpapers.presentation.shared.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.chignonMignon.wallpapers.presentation.shared.R
import com.chignonMignon.wallpapers.presentation.shared.databinding.ViewAboutBinding
import com.chignonMignon.wallpapers.presentation.utilities.extensions.dimension

class AboutView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = ViewAboutBinding.inflate(LayoutInflater.from(context), this)

    init {
        setPadding(paddingLeft, paddingTop, paddingRight, context.dimension(R.dimen.about_vertical_margin))
        binding.linksBusiness.buttonWebsite.setOnClickListener { } // TODO
        binding.linksBusiness.buttonEmail.setOnClickListener { } // TODO
        binding.linksBusiness.buttonFacebook.setOnClickListener { } // TODO
        binding.linksBusiness.buttonInstagram.setOnClickListener { } // TODO
        binding.linksApplication.buttonGooglePlay.setOnClickListener { } // TODO
        binding.linksApplication.buttonGithub.setOnClickListener { } // TODO
        binding.linksApplication.buttonLicenses.setOnClickListener { } // TODO
    }
}