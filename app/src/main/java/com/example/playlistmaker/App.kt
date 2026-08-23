package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.data.localcache.CacheRepositoryProvider
import com.example.playlistmaker.domain.repository.CacheRepository

class App : Application() {

    private var theme = false
    private lateinit var cacheRepository: CacheRepository

    override fun onCreate() {
        super.onCreate()
        cacheRepository = CacheRepositoryProvider.provideCacheRepository(this)
        val savedValue = cacheRepository.getThemeValue()
        switchTheme(savedValue)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        theme = darkThemeEnabled
        cacheRepository.setThemeValue(theme)
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}