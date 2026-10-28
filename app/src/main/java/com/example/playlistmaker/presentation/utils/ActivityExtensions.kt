package com.example.playlistmaker.presentation.utils

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.res.Configuration
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.WindowCompat

fun Activity.isNightMode(): Boolean {
    val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return currentNightMode == Configuration.UI_MODE_NIGHT_YES
}

fun EditText.hideKeyboard(activity: Activity) {
    val inputMethodManager = activity.getSystemService(
        INPUT_METHOD_SERVICE
    ) as? InputMethodManager
    inputMethodManager?.hideSoftInputFromWindow(this.windowToken, 0)
}

fun Activity.checkTheme(settings: FragmentThemeSettings) {
    val controller = WindowCompat.getInsetsController(
        window,
        window.decorView
    )
    if (isNightMode()) {
        controller.isAppearanceLightStatusBars = settings.lightSB
        controller.isAppearanceLightNavigationBars = settings.lightNB
    } else {
        controller.isAppearanceLightStatusBars = settings.darkSB
        controller.isAppearanceLightNavigationBars = settings.darkNB
    }
}

abstract class FragmentThemeSettings(
    open val lightSB: Boolean, open val darkSB: Boolean,
    open val lightNB: Boolean, open val darkNB: Boolean
)

data class FragmentTheme(
    override val lightSB: Boolean, override val darkSB: Boolean,
    override val lightNB: Boolean, override val darkNB: Boolean
) : FragmentThemeSettings(lightSB, darkSB, lightNB, darkNB)
