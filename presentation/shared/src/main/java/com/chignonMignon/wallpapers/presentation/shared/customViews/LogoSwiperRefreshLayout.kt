package com.chignonMignon.wallpapers.presentation.shared.customViews

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chignonMignon.wallpapers.presentation.shared.R

class LogoSwiperRefreshLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : SwipeRefreshLayout(context, attrs) {

    init {
        (getChildAt(0) as ImageView).setImageResource(R.drawable.img_loading_indicator_interior)
    }
}