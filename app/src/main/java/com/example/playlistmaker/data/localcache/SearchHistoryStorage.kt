package com.example.playlistmaker.data.localcache

import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.data.localcache.PreferenceStorage.Companion.SHARED_KEY_CACHE
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryStorage(
    private val preferenceStorage: PreferenceStorage,
) {

    private val gson = Gson()

    fun getHistory(): List<TrackDto> {
        val json = preferenceStorage.getString(SHARED_KEY_CACHE) ?: return emptyList()
        val type = object : TypeToken<List<TrackDto>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveHistory(tracks: List<TrackDto>) {
        preferenceStorage.putString(SHARED_KEY_CACHE, gson.toJson(tracks))
    }

    fun clearHistory() {
        preferenceStorage.putString(SHARED_KEY_CACHE, gson.toJson(emptyList<TrackDto>()))
    }

}