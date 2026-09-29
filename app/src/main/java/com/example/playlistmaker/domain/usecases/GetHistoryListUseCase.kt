package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.repository.SearchHistoryRepository

class GetHistoryListUseCase(private val repository: SearchHistoryRepository) {
    suspend operator fun invoke(): List<Track> = repository.getHistory()
}