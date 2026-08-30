package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.Creator

class App : Application() {

    private var theme = false
    private val getThemeUseCase by lazy {
        Creator.provideThemeUseCase(this)
    }
    private val updateThemeUseCase by lazy {
        Creator.provideUpdateThemeUseCase(this)
    }

    override fun onCreate() {
        super.onCreate()
        val savedValue = getThemeUseCase()
        switchTheme(savedValue)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        theme = darkThemeEnabled
        updateThemeUseCase(theme)
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}