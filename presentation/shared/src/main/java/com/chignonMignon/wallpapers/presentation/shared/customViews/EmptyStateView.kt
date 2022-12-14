package com.chignonMignon.wallpapers.presentation.shared.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.chignonMignon.wallpapers.presentation.shared.databinding.ViewEmptyStateBinding

class EmptyStateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        ViewEmptyStateBinding.inflate(LayoutInflater.from(context), this)
    }
}