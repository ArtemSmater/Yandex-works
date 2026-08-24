package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.entities.Track

interface CacheRepository {
    fun getThemeValue() : Boolean
    fun setThemeValue(isNight: Boolean)
    fun getTrackList(): List<Track>
    fun fillCacheList(tracks: List<Track>)
}