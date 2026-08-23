package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.utils.PrefsUtil
import androidx.core.content.edit

class App : Application() {

    private var theme = false
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(PrefsUtil.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
        val savedValue = sharedPreferences.getBoolean(PrefsUtil.SHARED_KEY_THEME, false)
        switchTheme(savedValue)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        theme = darkThemeEnabled
        sharedPreferences.edit { putBoolean(PrefsUtil.SHARED_KEY_THEME, theme) }
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

    }
}