package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.repository.SearchHistoryRepository

class AddTrackToSearchHistoryUseCase(private val repository: SearchHistoryRepository) {
    suspend operator fun invoke(track: Track) {
        repository.saveToHistory(track)
    }
}