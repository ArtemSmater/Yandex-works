package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.repository.ThemeRepository
class GetThemeUseCase(private val repository: ThemeRepository) {
    operator fun invoke(): Boolean {
        return repository.getThemeValue()
    }
}