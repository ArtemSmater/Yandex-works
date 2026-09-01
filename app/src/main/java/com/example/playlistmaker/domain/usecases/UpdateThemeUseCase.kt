package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.repository.ThemeRepository
class UpdateThemeUseCase(private val repository: ThemeRepository) {
    operator fun invoke(isNight: Boolean) {
        repository.setThemeValue(isNight)
    }
}