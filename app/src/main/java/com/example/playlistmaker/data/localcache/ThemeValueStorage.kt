package com.example.playlistmaker.data.localcache

import com.example.playlistmaker.data.localcache.PreferenceStorage.Companion.SHARED_KEY_THEME


class ThemeValueStorage(
    private val preferenceStorage: PreferenceStorage,
) {

    fun getTheme(): Boolean {
        return preferenceStorage.getBoolean(SHARED_KEY_THEME, false)
    }

    fun setTheme(isNight: Boolean) {
        preferenceStorage.putBoolean(SHARED_KEY_THEME, isNight)
    }
}