package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.repository.CacheRepository

class UpdateCacheUseCase(private val cacheRepository: CacheRepository) {
    operator fun invoke(tracks: List<Track>) {
        return cacheRepository.fillCacheList(tracks)
    }
}