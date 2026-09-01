package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.repository.HistoryRepository

class AddTrackToSearchHistoryUseCase(
    private val repository: HistoryRepository
) {
    operator fun invoke(track: Track) {
        val currentHistory = repository.getHistory().toMutableList()
        val updatedHistory = listOf(track) + currentHistory
            .filter { it.trackId != track.trackId }
            .take(9)
        repository.saveHistory(updatedHistory)
    }
}