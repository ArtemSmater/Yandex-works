package com.example.playlistmaker.presentation.ui.settings

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.domain.usecases.GetThemeUseCase

class SettingsViewModelFactory(
    private val getThemeUseCase: GetThemeUseCase,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            getThemeUseCase,
            application
        ) as T
    }
}