package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.Creator

class App : Application() {

    private var theme = false

    override fun onCreate() {
        super.onCreate()
        Creator.init(this)
        val savedValue = Creator.getThemeUseCase()
        switchTheme(savedValue)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        theme = darkThemeEnabled
        Creator.getUpdateThemeUseCase(theme)
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}