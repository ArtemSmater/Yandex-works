package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.repository.LocalThemeRepository
class UpdateThemeUseCase(private val repository: LocalThemeRepository) {
    operator fun invoke(isNight: Boolean) {
        repository.setThemeValue(isNight)
    }
}