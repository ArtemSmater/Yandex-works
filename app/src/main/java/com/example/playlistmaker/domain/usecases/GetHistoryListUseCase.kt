package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.repository.HistoryRepository

class GetHistoryListUseCase(private val repository: HistoryRepository) {
    operator fun invoke(): List<Track> {
        return repository.getHistory()
    }
}