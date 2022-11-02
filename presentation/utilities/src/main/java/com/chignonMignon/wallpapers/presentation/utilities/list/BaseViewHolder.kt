package com.chignonMignon.wallpapers.presentation.utilities.list

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<LI : ListItem, B : ViewDataBinding>(protected val binding: B) : RecyclerView.ViewHolder(binding.root) {

    @Suppress("UNCHECKED_CAST")
    internal fun bindInternal(listItem: Any) = bind(listItem as LI)

    abstract fun bind(listItem: LI)
}