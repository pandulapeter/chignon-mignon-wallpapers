package com.chignonMignon.wallpapers.presentation.utilities.list

import androidx.annotation.CallSuper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class BaseListAdapter<LI : ListItem> : ListAdapter<LI, BaseViewHolder<out LI, *>>(object : DiffUtil.ItemCallback<LI>() {

    override fun areItemsTheSame(oldItem: LI, newItem: LI) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: LI, newItem: LI) = oldItem == newItem

    override fun getChangePayload(oldItem: LI, newItem: LI) = ""
}) {

    init {
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    @CallSuper
    override fun onBindViewHolder(holder: BaseViewHolder<out LI, *>, position: Int) = holder.bindInternal(getItem(position))
}