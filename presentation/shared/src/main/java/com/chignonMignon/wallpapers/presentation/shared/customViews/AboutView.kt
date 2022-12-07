package com.chignonMignon.wallpapers.presentation.shared.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.chignonMignon.wallpapers.presentation.shared.R
import com.chignonMignon.wallpapers.presentation.shared.databinding.ViewAboutBinding
import com.chignonMignon.wallpapers.presentation.shared.extensions.openEmailComposer
import com.chignonMignon.wallpapers.presentation.shared.extensions.openUrl
import com.chignonMignon.wallpapers.presentation.utilities.extensions.dimension


class AboutView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = ViewAboutBinding.inflate(LayoutInflater.from(context), this)

    init {
        setPadding(paddingLeft, paddingTop, paddingRight, context.dimension(R.dimen.about_vertical_margin) + context.dimension(R.dimen.double_content_padding))
        binding.linksBusiness.buttonWebsite.setOnClickListener { context.openUrl("https://chignonmignon.ro/", this) }
        binding.linksBusiness.buttonEmail.setOnClickListener { context.openEmailComposer("office@chignonmignon.ro", this) }
        binding.linksBusiness.buttonFacebook.setOnClickListener { context.openUrl("https://www.facebook.com/chignon.mignon/", this) }
        binding.linksBusiness.buttonInstagram.setOnClickListener { context.openUrl("https://www.instagram.com/chignonmignon/", this) }
        binding.linksApplication.buttonGooglePlay.setOnClickListener { } // TODO
        binding.linksApplication.buttonGithub.setOnClickListener { context.openUrl("https://github.com/pandulapeter/chignon-mignon-wallpapers", this) }
        binding.linksApplication.buttonLicenses.setOnClickListener { } // TODO
    }
}