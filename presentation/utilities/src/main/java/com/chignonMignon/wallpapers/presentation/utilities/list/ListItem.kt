package com.chignonMignon.wallpapers.presentation.utilities.list

interface ListItem {

    val id: String

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int
}