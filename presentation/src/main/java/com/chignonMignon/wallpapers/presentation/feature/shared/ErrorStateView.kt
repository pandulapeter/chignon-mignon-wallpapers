package com.chignonMignon.wallpapers.presentation.feature.shared

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.chignonMignon.wallpapers.presentation.databinding.ViewErrorStateBinding

internal class ErrorStateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = ViewErrorStateBinding.inflate(LayoutInflater.from(context), this)

    override fun setOnClickListener(onClickListener: OnClickListener?) =
        binding.button.setOnClickListener(onClickListener)
}