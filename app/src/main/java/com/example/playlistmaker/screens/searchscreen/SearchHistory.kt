package com.example.playlistmaker.screens.searchscreen

import android.content.SharedPreferences
import com.example.playlistmaker.pojo.Track
import com.example.playlistmaker.utils.PrefsUtil
import com.google.gson.Gson
import androidx.core.content.edit
import com.example.playlistmaker.interfaces.CacheSubscriber
import com.google.gson.reflect.TypeToken

class SearchHistory(
    private val sharedPreferences: SharedPreferences,
    cacheSubscriber: CacheSubscriber
) {

    init {
        cacheSubscriber.getCacheTracks(read(sharedPreferences).toMutableList())
    }

    fun fillCache(tracks: List<Track>) {
        write(sharedPreferences, tracks)
    }

    private fun read(sharedPreferences: SharedPreferences): List<Track> {
        val json = sharedPreferences.getString(PrefsUtil.SHARED_KEY_CACHE, null) ?: return listOf()
        val type = object : TypeToken<List<Track>>() {}.type
        return Gson().fromJson(json, type)
    }

    // запись
    private fun write(sharedPreferences: SharedPreferences, tracks: List<Track>) {
        val json = Gson().toJson(tracks)
        sharedPreferences.edit { putString(PrefsUtil.SHARED_KEY_CACHE, json) }
    }
}