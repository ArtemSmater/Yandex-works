package com.example.playlistmaker.presentation.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.domain.repository.CacheRepository
import com.example.playlistmaker.domain.usecases.GetThemeUseCase
import com.example.playlistmaker.domain.usecases.UpdateThemeUseCase

class SettingsViewModelFactory(
    private val cacheRepository: CacheRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            GetThemeUseCase(cacheRepository),
            UpdateThemeUseCase(cacheRepository)
        ) as T
    }
}