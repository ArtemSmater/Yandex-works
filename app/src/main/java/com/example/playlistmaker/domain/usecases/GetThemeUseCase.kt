package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.repository.CacheRepository

class GetThemeUseCase(private val cacheRepository: CacheRepository) {
    operator fun invoke() : Boolean {
        return cacheRepository.getThemeValue()
    }
}