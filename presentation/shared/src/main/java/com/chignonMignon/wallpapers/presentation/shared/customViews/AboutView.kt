package com.chignonMignon.wallpapers.presentation.shared.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.chignonMignon.wallpapers.presentation.shared.R
import com.chignonMignon.wallpapers.presentation.shared.databinding.ViewAboutBinding
import com.chignonMignon.wallpapers.presentation.utilities.extensions.dimension

class AboutView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL
        ViewAboutBinding.inflate(LayoutInflater.from(context), this)
        context.dimension(R.dimen.double_content_padding).let {
            // TODO: Bottom padding should take into account the bottom inset
            setPadding(paddingLeft, it, paddingRight, it)
        }
    }
}