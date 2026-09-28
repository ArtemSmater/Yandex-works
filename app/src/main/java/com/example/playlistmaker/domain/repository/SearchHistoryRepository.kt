package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.entities.Track

interface SearchHistoryRepository {

    fun saveToHistory(track: Track)
    fun getHistory() : List<Track>
    fun clearHistory()
}