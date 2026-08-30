package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.repository.TrackRepository

class GetCacheListUseCase(private val repository: TrackRepository) {
    operator fun invoke(): List<Track> {
        return repository.getTrackList()
    }
}