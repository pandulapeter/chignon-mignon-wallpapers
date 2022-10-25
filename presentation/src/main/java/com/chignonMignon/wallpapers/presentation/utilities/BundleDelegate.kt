package com.chignonMignon.wallpapers.presentation.utilities

import android.os.Bundle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal sealed class BundleDelegate<T>(protected val key: kotlin.String, protected val defaultValue: T) : ReadWriteProperty<Bundle?, T> {

    class String(key: kotlin.String, defaultValue: kotlin.String = "") : BundleDelegate<kotlin.String>(key, defaultValue) {

        override fun getValue(thisRef: Bundle?, property: KProperty<*>) = thisRef?.getString(key, defaultValue) ?: defaultValue

        override fun setValue(thisRef: Bundle?, property: KProperty<*>, value: kotlin.String) = thisRef?.putString(key, value) ?: Unit
    }

    class Parcelable<T>(key: kotlin.String, defaultValue: android.os.Parcelable? = null) : BundleDelegate<android.os.Parcelable?>(key, defaultValue) {

        override fun getValue(thisRef: Bundle?, property: KProperty<*>) = thisRef?.getParcelable(key) ?: defaultValue

        override fun setValue(thisRef: Bundle?, property: KProperty<*>, value: android.os.Parcelable?) = thisRef?.putParcelable(key, value) ?: Unit
    }
}