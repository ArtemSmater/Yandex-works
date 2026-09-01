package com.example.playlistmaker.data.repositories

import com.example.playlistmaker.data.localcache.SearchHistoryStorage
import com.example.playlistmaker.data.mapper.TrackMapper
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.repository.HistoryRepository

class HistoryRepositoryImpl(
    private val storage: SearchHistoryStorage
) : HistoryRepository {
    private val mapper = TrackMapper()

    override fun getHistory(): List<Track> {
        return mapper.mapDtoListToEntityList(storage.getHistory()) ?: emptyList()
    }

    override fun saveHistory(tracks: List<Track>) {
        storage.saveHistory(mapper.mapEntityListToDtoList(tracks))
    }

    override fun clearHistory() {
        storage.clearHistory()
    }
}