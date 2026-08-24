package com.example.playlistmaker.presentation.utils

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.res.Configuration
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
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

fun EditText.hideKeyboard(activity: Activity) {
    val inputMethodManager = activity
        .getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
    inputMethodManager
        ?.hideSoftInputFromWindow(this.windowToken, 0)
}
