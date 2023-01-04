package com.chignonMignon.wallpapers.presentation.wallpaperDetails.implementation.wallpaperList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.fragment.app.FragmentManager
import com.chignonMignon.wallpapers.presentation.utilities.BundleDelegate
import com.chignonMignon.wallpapers.presentation.utilities.extensions.autoClearedValue
import com.chignonMignon.wallpapers.presentation.utilities.extensions.withArguments
import com.chignonMignon.wallpapers.presentation.wallpaperDetails.databinding.FragmentWallpaperTypeSelectorBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
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
        arguments?.backgroundColor?.let(binding.card::setCardBackgroundColor)
        binding.homeScreen.setOnClickListener { onHomeScreenSelected() }
        binding.lockScreen.setOnClickListener { onLockScreenSelected() }
        binding.both.setOnClickListener { onBothSelected() }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) = super.onCreateDialog(savedInstanceState).apply {
        setOnShowListener { dialog ->
            (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.let {
                it.background = null
                BottomSheetBehavior.from(it).run {
                    state = BottomSheetBehavior.STATE_EXPANDED
                    skipCollapsed = true
                }
            }
        }
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
        private var Bundle.backgroundColor by BundleDelegate.Int("backgroundColor")

        fun show(
            fragmentManager: FragmentManager,
            @ColorInt backgroundColor: Int
        ) = WallpaperTypeSelectorBottomSheetFragment().withArguments {
            it.backgroundColor = backgroundColor
        }.show(fragmentManager, null)
    }
}