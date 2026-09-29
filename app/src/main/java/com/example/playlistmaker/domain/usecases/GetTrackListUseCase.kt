package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.repository.TrackRepository

class GetTrackListUseCase(private val repository: TrackRepository) {
    suspend operator fun invoke(query: String): List<Track> {
        return repository.getTrackList(query)
    }
}