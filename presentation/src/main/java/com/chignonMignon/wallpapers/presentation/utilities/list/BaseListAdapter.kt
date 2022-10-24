package com.chignonMignon.wallpapers.presentation.utilities.list

import androidx.annotation.CallSuper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

internal abstract class BaseListAdapter<LI : ListItem> : ListAdapter<LI, BaseViewHolder<out LI, *>>(object : DiffUtil.ItemCallback<LI>() {

    override fun areItemsTheSame(oldItem: LI, newItem: LI) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: LI, newItem: LI) = oldItem == newItem

    override fun getChangePayload(oldItem: LI, newItem: LI) = ""
}) {

    @CallSuper
    override fun onBindViewHolder(holder: BaseViewHolder<out LI, *>, position: Int) = holder.bind(getItem(position))
}