package com.example.playlistmaker.data.localcache

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.presentation.utils.PrefsUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PreferenceStorage(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(
        PrefsUtil.SHARED_PREFERENCE_NAME,
        MODE_PRIVATE
    )

    fun getTheme() : Boolean {
        return sharedPreferences.getBoolean(PrefsUtil.SHARED_KEY_THEME, false)
    }

    fun setTheme(isNight: Boolean) {
        sharedPreferences.edit { putBoolean(PrefsUtil.SHARED_KEY_THEME, isNight) }
    }

    fun getCache() : List<Track> {
        return read(sharedPreferences).toMutableList()
    }

    fun fillCache(tracks: List<Track>) {
        write(sharedPreferences, tracks)
    }

    private fun read(sharedPreferences: SharedPreferences): List<Track> {
        val json = sharedPreferences.getString(PrefsUtil.SHARED_KEY_CACHE, null) ?: return listOf()
        val type = object : TypeToken<List<Track>>() {}.type
        return Gson().fromJson(json, type)
    }

    private fun write(sharedPreferences: SharedPreferences, tracks: List<Track>) {
        val json = Gson().toJson(tracks)
        sharedPreferences.edit { putString(PrefsUtil.SHARED_KEY_CACHE, json) }
    }
}