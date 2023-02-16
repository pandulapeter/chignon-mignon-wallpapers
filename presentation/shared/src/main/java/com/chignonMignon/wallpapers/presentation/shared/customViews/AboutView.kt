package com.chignonMignon.wallpapers.presentation.shared.customViews

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.chignonMignon.wallpapers.presentation.shared.R
import com.chignonMignon.wallpapers.presentation.shared.databinding.ViewAboutBinding
import com.chignonMignon.wallpapers.presentation.shared.extensions.openEmailComposer
import com.chignonMignon.wallpapers.presentation.shared.extensions.openUrl
import com.chignonMignon.wallpapers.presentation.shared.extensions.showSnackbar
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
        binding.linksApplication.buttonGooglePlay.setOnClickListener { context.openPlayStoreListing(this) }
        binding.linksApplication.buttonGithub.setOnClickListener { context.openUrl("https://github.com/pandulapeter/chignon-mignon-wallpapers", this) }
        binding.linksApplication.buttonPrivacyPolicy.setOnClickListener {  context.openUrl("https://chignonmignon.ro/", this) } // TODO
        binding.linksApplication.buttonBugReport.setOnClickListener { context.openEmailComposer("pandulapeter@gmail.com", this) }
    }

    private fun Context.openPlayStoreListing(view: View) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$PACKAGE_NAME")))
        } catch (_: ActivityNotFoundException) {
            openUrl("https://play.google.com/store/apps/details?id=$PACKAGE_NAME", view)
        }
    }

    companion object {
        private const val PACKAGE_NAME = "com.chignonMignon.wallpapers"
    }
}