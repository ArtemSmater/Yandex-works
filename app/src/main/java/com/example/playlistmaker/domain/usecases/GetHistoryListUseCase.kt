package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.repository.SearchHistoryRepository
import javax.inject.Inject

class GetHistoryListUseCase @Inject constructor(
    private val repository: SearchHistoryRepository
) {
    suspend operator fun invoke(): List<Track> = repository.getHistory()
}