package com.example.playlistmaker.data.repositories

import com.example.playlistmaker.data.dto.local.TrackEntity
import com.example.playlistmaker.data.local.StorageClient
import com.example.playlistmaker.data.mapper.mapDomainListToEntityList
import com.example.playlistmaker.data.mapper.mapEntityListToDomainList
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.repository.SearchHistoryRepository

class SearchHistoryRepositoryImpl(
    private val storage: StorageClient<List<TrackEntity>>
) : SearchHistoryRepository {
    override fun saveToHistory(track: Track) {
        val currentHistory = getHistory().toMutableList()
        val updatedHistory = listOf(track) + currentHistory
            .filter { it.trackId != track.trackId }
            .take(9)
        storage.saveData(updatedHistory.mapDomainListToEntityList())
    }

    override fun getHistory(): List<Track> {
        return storage.getData()?.mapEntityListToDomainList() ?: emptyList()
    }

    override fun clearHistory() {
        storage.saveData(emptyList())
    }
}