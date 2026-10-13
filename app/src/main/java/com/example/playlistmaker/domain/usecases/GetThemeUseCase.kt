package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.repository.LocalThemeRepository
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(
    private val repository: LocalThemeRepository
) {
    operator fun invoke(): Boolean = repository.getThemeValue()
}