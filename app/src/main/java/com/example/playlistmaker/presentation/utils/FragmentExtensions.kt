package com.example.playlistmaker.presentation.utils

import android.content.res.Configuration
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment

fun Fragment.configureSystemBars(
    lightStatusBarIcons: Boolean,
    lightNavigationBarIcons: Boolean
) {
    val controller = WindowCompat.getInsetsController(
        requireActivity().window,
        requireActivity().window.decorView
    )

    controller.isAppearanceLightStatusBars = lightStatusBarIcons
    controller.isAppearanceLightNavigationBars = lightNavigationBarIcons
}

fun Fragment.isNightMode(): Boolean {
    val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return currentNightMode == Configuration.UI_MODE_NIGHT_YES
}
