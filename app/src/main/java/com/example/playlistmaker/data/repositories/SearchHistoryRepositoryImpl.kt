package com.example.playlistmaker.data.repositories

import com.example.playlistmaker.data.local.database.tables.TrackEntity
import com.example.playlistmaker.data.local.cache.StorageClient
import com.example.playlistmaker.data.mapper.mapDomainListToEntityList
import com.example.playlistmaker.data.mapper.mapEntityListToDomainList
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchHistoryRepositoryImpl @Inject constructor(
    private val storage: StorageClient<List<TrackEntity>>
) : SearchHistoryRepository {

    private val mutex = Mutex()

    override suspend fun saveToHistory(track: Track) {
        withContext(Dispatchers.IO) {
            mutex.withLock {
                val currentHistory = getHistory().toMutableList()

                val updatedHistory = listOf(track) + currentHistory
                    .filter { it.trackId != track.trackId }
                    .take(9)
                storage.saveData(updatedHistory.mapDomainListToEntityList())
            }
        }
    }

    override suspend fun getHistory(): List<Track> {
        return withContext(Dispatchers.IO) {
            storage.getData()?.mapEntityListToDomainList() ?: emptyList()
        }
    }

    override suspend fun clearHistory() {
        withContext(Dispatchers.IO) {
            mutex.withLock {
                storage.saveData(emptyList())
            }
        }
    }
}