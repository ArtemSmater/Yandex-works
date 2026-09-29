package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.entities.Track

interface SearchHistoryRepository {

    suspend fun saveToHistory(track: Track)
    suspend fun getHistory() : List<Track>
    suspend fun clearHistory()
}