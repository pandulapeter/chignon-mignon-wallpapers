package com.chignonMignon.wallpapers.presentation.utilities

import android.os.Bundle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

sealed class BundleDelegate<T>(protected val key: kotlin.String, protected val defaultValue: T) : ReadWriteProperty<Bundle?, T> {

    class Int(key: kotlin.String, defaultValue: kotlin.Int = -1) : BundleDelegate<kotlin.Int>(key, defaultValue) {

        override fun getValue(thisRef: Bundle?, property: KProperty<*>) = thisRef?.getInt(key, defaultValue) ?: defaultValue

        override fun setValue(thisRef: Bundle?, property: KProperty<*>, value: kotlin.Int) = thisRef?.putInt(key, value) ?: Unit
    }

    class String(key: kotlin.String, defaultValue: kotlin.String = "") : BundleDelegate<kotlin.String>(key, defaultValue) {

        override fun getValue(thisRef: Bundle?, property: KProperty<*>) = thisRef?.getString(key, defaultValue) ?: defaultValue

        override fun setValue(thisRef: Bundle?, property: KProperty<*>, value: kotlin.String) = thisRef?.putString(key, value) ?: Unit
    }

    class Parcelable<T: android.os.Parcelable>(key: kotlin.String, defaultValue: T? = null) : BundleDelegate<T?>(key, defaultValue) {

        override fun getValue(thisRef: Bundle?, property: KProperty<*>) = thisRef?.getParcelable(key) ?: defaultValue

        override fun setValue(thisRef: Bundle?, property: KProperty<*>, value: T?) = thisRef?.putParcelable(key, value) ?: Unit
    }

    class ParcelableList<T: android.os.Parcelable>(key: kotlin.String, defaultValue: List<T> = emptyList()) : BundleDelegate<List<T>>(key, defaultValue) {

        override fun getValue(thisRef: Bundle?, property: KProperty<*>) = thisRef?.getParcelableArrayList(key) ?: defaultValue

        override fun setValue(thisRef: Bundle?, property: KProperty<*>, value: List<T>) = thisRef?.putParcelableArrayList(key, ArrayList(value)) ?: Unit
    }
}