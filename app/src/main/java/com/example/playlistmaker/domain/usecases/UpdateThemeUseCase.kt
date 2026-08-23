package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.repository.CacheRepository

class UpdateThemeUseCase(private val cacheRepository: CacheRepository) {
    operator fun invoke(isNight: Boolean) {
        cacheRepository.setThemeValue(isNight)
    }
}