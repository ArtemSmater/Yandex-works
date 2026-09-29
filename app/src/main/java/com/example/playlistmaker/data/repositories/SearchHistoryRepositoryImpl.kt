package com.example.playlistmaker.data.repositories

import com.example.playlistmaker.data.dto.local.TrackEntity
import com.example.playlistmaker.data.local.StorageClient
import com.example.playlistmaker.data.mapper.mapDomainListToEntityList
import com.example.playlistmaker.data.mapper.mapEntityListToDomainList
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchHistoryRepositoryImpl(
    private val storage: StorageClient<List<TrackEntity>>
) : SearchHistoryRepository {
    override suspend fun saveToHistory(track: Track) {
        val currentHistory = getHistory().toMutableList()
        withContext(Dispatchers.IO) {
            val updatedHistory = listOf(track) + currentHistory
                .filter { it.trackId != track.trackId }
                .take(9)
            storage.saveData(updatedHistory.mapDomainListToEntityList())
        }
    }

    override suspend fun getHistory(): List<Track> {
        return withContext(Dispatchers.IO) {
            storage.getData()?.mapEntityListToDomainList() ?: emptyList()
        }
    }

    override suspend fun clearHistory() {
        withContext(Dispatchers.IO) {
            storage.saveData(emptyList())
        }
    }
}