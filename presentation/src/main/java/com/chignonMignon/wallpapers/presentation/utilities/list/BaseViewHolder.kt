package com.chignonMignon.wallpapers.presentation.utilities.list

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.chignonMignon.wallpapers.presentation.BR

internal abstract class BaseViewHolder<LI : ListItem, B : ViewDataBinding>(protected val binding: B) : RecyclerView.ViewHolder(binding.root) {

    internal fun bind(listItem: Any) {
        binding.setVariable(BR.uiModel, listItem)
    }
}