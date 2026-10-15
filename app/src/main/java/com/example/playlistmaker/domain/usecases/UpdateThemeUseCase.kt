package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.repository.LocalThemeRepository
import javax.inject.Inject

class UpdateThemeUseCase @Inject constructor(
    private val repository: LocalThemeRepository
) {
    operator fun invoke(isNight: Boolean) {
        repository.setThemeValue(isNight)
    }
}