package com.example.playlistmaker.data.repositories

import com.example.playlistmaker.data.localcache.ThemeValueStorage
import com.example.playlistmaker.domain.repository.ThemeRepository

class ThemeRepositoryImpl(
    private val storage: ThemeValueStorage
) : ThemeRepository {

    override fun getThemeValue(): Boolean {
        return storage.getTheme()
    }

    override fun setThemeValue(isNight: Boolean) {
        storage.setTheme(isNight)
    }
}