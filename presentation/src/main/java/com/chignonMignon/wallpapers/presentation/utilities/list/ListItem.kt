package com.chignonMignon.wallpapers.presentation.utilities.list

internal interface ListItem {

    val id: String

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int
}