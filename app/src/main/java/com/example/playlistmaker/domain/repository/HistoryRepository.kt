package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.entities.Track

interface HistoryRepository {
    fun getHistory() : List<Track>
    fun saveHistory(tracks: List<Track>)
    fun clearHistory()
}