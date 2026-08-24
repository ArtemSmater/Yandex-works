package com.example.playlistmaker.data.repositories

import com.example.playlistmaker.data.localcache.PreferenceStorage
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.repository.CacheRepository

class CacheRepositoryImpl(
    private val storage: PreferenceStorage
) : CacheRepository {
    override fun getThemeValue(): Boolean {
        return storage.getTheme()
    }

    override fun setThemeValue(isNight: Boolean) {
        storage.setTheme(isNight)
    }

    override fun getTrackList(): List<Track> {
        return storage.getCache()
    }

    override fun fillCacheList(tracks: List<Track>) {
        storage.fillCache(tracks)
    }
}