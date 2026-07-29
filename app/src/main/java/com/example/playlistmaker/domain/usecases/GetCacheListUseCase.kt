package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.repository.CacheRepository

class GetCacheListUseCase(private val repository: CacheRepository) {
    operator fun invoke(): List<Track> {
        return repository.getTrackList()
    }
}