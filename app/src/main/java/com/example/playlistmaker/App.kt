package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.ApplicationComponent
import com.example.playlistmaker.di.DaggerApplicationComponent

class App : Application() {

    private var theme = false

    lateinit var appComponent: ApplicationComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.factory().create(this)
        val savedValue = appComponent.getThemeUseCase().invoke()
        switchTheme(savedValue)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        theme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}