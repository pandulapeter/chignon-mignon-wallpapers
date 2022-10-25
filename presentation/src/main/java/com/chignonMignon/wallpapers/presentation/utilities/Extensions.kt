package com.chignonMignon.wallpapers.presentation.utilities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.chignonMignon.wallpapers.data.model.domain.TranslatableText
import com.chignonMignon.wallpapers.presentation.feature.Navigator
import com.chignonMignon.wallpapers.presentation.feature.shared.ColorPaletteGenerator
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Locale

inline fun <reified T : Fragment> FragmentManager.handleReplace(
    @IdRes containerId: Int,
    addToBackStack: Boolean = false,
    tag: String = T::class.java.name,
    crossinline newInstance: () -> T
) {
    beginTransaction().apply {
        findFragmentById(containerId)?.run {
            exitTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true)
            reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, false)
        }
        val newFragment = findFragmentByTag(tag) ?: newInstance()
        newFragment.enterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true)
        newFragment.returnTransition = MaterialSharedAxis(MaterialSharedAxis.Y, false)
        replace(containerId, newFragment, tag)
        if (addToBackStack) {
            addToBackStack(null)
        }
        setReorderingAllowed(true)
        commitAllowingStateLoss()
    }
}

internal fun Context.color(@ColorRes colorResourceId: Int) = ContextCompat.getColor(this, colorResourceId)

internal fun Context.dimension(@DimenRes dimensionResourceId: Int) = resources.getDimensionPixelSize(dimensionResourceId)

internal fun Context.getWallpapersFolder() = File(filesDir, "wallpapers").also { it.mkdirs() }

internal fun Context.getWallpaperFile(id: String) =File("${getWallpapersFolder().path}/${id}.png")

internal fun Context.getUriForFile(file: File) =
    FileProvider.getUriForFile(applicationContext, applicationContext.packageName + ".fileProvider", file)

internal suspend fun Context.downloadImage(url: String) = ((ImageLoader(this).execute(
    ImageRequest.Builder(this).data(url).allowHardware(false).build()
) as? SuccessResult)?.drawable as? BitmapDrawable)?.bitmap

internal fun Context.saveImage(file: File, bitmap: Bitmap): Uri? {
    val outputBytes = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputBytes)
    if (file.exists()) {
        file.delete()
    }
    return try {
        file.createNewFile()
        val fileOutputStream = FileOutputStream(file)
        fileOutputStream.write(outputBytes.toByteArray())
        fileOutputStream.close()
        getUriForFile(file)
    } catch (exception: IOException) {
        exception.printStackTrace()
        null
    }
}

internal val Fragment.navigator get() = activity as? Navigator

internal inline fun <reified B : ViewDataBinding> Fragment.bind(view: View? = null): B =
    DataBindingUtil.bind<B>(view ?: this.view ?: throw IllegalStateException("Fragment doesn't have a View."))?.apply {
        lifecycleOwner = viewLifecycleOwner
    } ?: throw IllegalStateException("No ViewDataBinding of instance: ${B::class} bound to the Fragment's View.")

internal inline fun <T : Fragment> T.withArguments(bundleOperations: (Bundle) -> Unit): T = apply {
    arguments = Bundle().apply { bundleOperations(this) }
}

internal inline fun <T> Flow<T>.observe(lifecycleOwner: LifecycleOwner, crossinline callback: (T) -> Unit) =
    onEach { callback(it) }.launchIn(lifecycleOwner.lifecycle.coroutineScope)

internal fun <T> MutableSharedFlow<T>.pushEvent(event: T) {
    tryEmit(event)
}

internal fun TranslatableText.toNavigatorTranslatableText() = Navigator.TranslatableText(
    english = english,
    hungarian = hungarian,
    romanian = romanian
)

internal fun ColorPaletteGenerator.ColorPalette.toNavigatorColorPalette() = Navigator.ColorPalette(
    background = background,
    foreground = foreground
)

private fun Navigator.TranslatableText.toText() = when (Locale.getDefault().language) {
    "hu" -> hungarian
    "ro" -> romanian
    else -> english
}

@BindingAdapter("android:text")
internal fun TextView.setText(translatableText: Navigator.TranslatableText?) {
    text = translatableText?.toText()
}

@BindingAdapter("title")
internal fun Toolbar.setTitle(translatableText: Navigator.TranslatableText?) {
    title = translatableText?.toText()
}

@BindingAdapter("subtitle")
internal fun Toolbar.setSubtitle(translatableText: Navigator.TranslatableText?) {
    subtitle = translatableText?.toText()
}

@BindingAdapter("imageUrl")
internal fun ImageView.setImageUrl(imageUrl: String?) = load(imageUrl) {
    allowHardware(false)
}

@set:BindingAdapter("android:visibility")
internal var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }