package com.chignonMignon.wallpapers.presentation.feature.wallpaperDetails

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.chignonMignon.wallpapers.presentation.R
import com.chignonMignon.wallpapers.presentation.databinding.FragmentWallpaperDetailsBinding
import com.chignonMignon.wallpapers.presentation.feature.Navigator
import com.chignonMignon.wallpapers.presentation.utilities.BundleDelegate
import com.chignonMignon.wallpapers.presentation.utilities.bind
import com.chignonMignon.wallpapers.presentation.utilities.navigator
import com.chignonMignon.wallpapers.presentation.utilities.observe
import com.chignonMignon.wallpapers.presentation.utilities.withArguments
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class WallpaperDetailsFragment : Fragment(R.layout.fragment_wallpaper_details) {

    private val viewModel by viewModel<WallpaperDetailsViewModel> { parametersOf(arguments?.wallpaper) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = bind<FragmentWallpaperDetailsBinding>(view)
        binding.viewModel = viewModel
        binding.setupToolbar()
        viewModel.events.observe(viewLifecycleOwner, ::handleEvent)
    }

    private fun FragmentWallpaperDetailsBinding.setupToolbar() = toolbar.setNavigationOnClickListener {
        navigator?.navigateBack()
    }

    private fun handleEvent(event: WallpaperDetailsViewModel.Event) = when (event) {
        is WallpaperDetailsViewModel.Event.SetWallpaper -> setWallpaper(event.uri)
        WallpaperDetailsViewModel.Event.ShowErrorSnackbar -> showErrorSnackbar()
    }

    private fun setWallpaper(uri: Uri) = Intent.createChooser(
        Intent(Intent.ACTION_SET_WALLPAPER, uri)
            .addCategory(Intent.CATEGORY_DEFAULT)
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION), getString(R.string.wallpaper_details_set_wallpaper)
    ).let { intent ->
        for (resolveInfo in activity?.packageManager?.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).orEmpty()) {
            val packageName = resolveInfo.activityInfo.packageName
            context?.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(intent)
    }

    private fun showErrorSnackbar() {
        // TODO
    }

    companion object {
        private var Bundle.wallpaper by BundleDelegate.Parcelable<Navigator.Wallpaper>("wallpaper")

        fun newInstance(wallpaper: Navigator.Wallpaper) = WallpaperDetailsFragment().withArguments {
            it.wallpaper = wallpaper
        }
    }
}