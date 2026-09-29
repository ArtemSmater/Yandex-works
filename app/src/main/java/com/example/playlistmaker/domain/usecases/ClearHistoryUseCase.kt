package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.repository.SearchHistoryRepository

class ClearHistoryUseCase(private val repository: SearchHistoryRepository) {
    suspend operator fun invoke() {
        repository.clearHistory()
    }
}