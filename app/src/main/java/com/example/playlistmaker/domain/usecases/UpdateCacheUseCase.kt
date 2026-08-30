package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.repository.TrackRepository

class UpdateCacheUseCase(private val repository: TrackRepository) {
    operator fun invoke(tracks: List<Track>) {
        return repository.fillCacheList(tracks)
    }
}