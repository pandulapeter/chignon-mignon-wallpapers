package com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.wallpaperList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.chignonMignon.wallpapers.presentation.utilities.extensions.autoClearedValue
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.databinding.FragmentWallpaperTypeSelectorBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

internal class WallpaperTypeSelectorBottomSheetFragment : BottomSheetDialogFragment() {

    private var binding by autoClearedValue<FragmentWallpaperTypeSelectorBottomSheetBinding>()
    private val onWallpaperTypeSelectedListener get() = parentFragment as? OnWallpaperTypeSelectedListener ?: activity as? OnWallpaperTypeSelectedListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        LayoutInflater.from(inflater.context).let { themedInflater ->
            FragmentWallpaperTypeSelectorBottomSheetBinding.inflate(themedInflater, container, false).also {
                binding = it
            }.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.homeScreen.setOnClickListener { onHomeScreenSelected() }
        binding.lockScreen.setOnClickListener { onLockScreenSelected() }
        binding.both.setOnClickListener { onBothSelected() }
    }

    private fun onHomeScreenSelected() {
        onWallpaperTypeSelectedListener?.onHomeScreenSelected()
        dismiss()
    }

    private fun onLockScreenSelected() {
        onWallpaperTypeSelectedListener?.onLockScreenSelected()
        dismiss()
    }

    private fun onBothSelected() {
        onWallpaperTypeSelectedListener?.onBothSelected()
        dismiss()
    }


    companion object {
        fun show(
            fragmentManager: FragmentManager,
        ) = WallpaperTypeSelectorBottomSheetFragment().show(fragmentManager, null)
    }
}