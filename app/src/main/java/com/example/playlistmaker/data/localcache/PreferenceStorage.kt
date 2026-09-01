package com.example.playlistmaker.data.localcache

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit

class PreferenceStorage(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(
        SHARED_PREFERENCE_NAME,
        MODE_PRIVATE
    )

    fun getString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun putString(key: String, value: String) {
        sharedPreferences.edit { putString(key, value) }
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit { putBoolean(key, value) }
    }

    companion object {
        const val SHARED_KEY_THEME = "key_of_theme_value"
        const val SHARED_PREFERENCE_NAME = "shared_preference_name"
        const val SHARED_KEY_CACHE = "key_of_track_cache_list"
    }
}