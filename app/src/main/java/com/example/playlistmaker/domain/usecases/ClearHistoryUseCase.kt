package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.repository.SearchHistoryRepository
import javax.inject.Inject

class ClearHistoryUseCase @Inject constructor(
    private val repository: SearchHistoryRepository
) {
    suspend operator fun invoke() {
        repository.clearHistory()
    }
}