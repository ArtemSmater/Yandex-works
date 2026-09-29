package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.repository.LocalThemeRepository

class GetThemeUseCase(private val repository: LocalThemeRepository) {
    operator fun invoke(): Boolean = repository.getThemeValue()
}