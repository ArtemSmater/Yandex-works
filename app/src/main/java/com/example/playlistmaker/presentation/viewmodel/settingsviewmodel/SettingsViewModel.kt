package com.example.playlistmaker.presentation.viewmodel.settingsviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.usecases.GetThemeUseCase
import com.example.playlistmaker.domain.usecases.UpdateThemeUseCase

class SettingsViewModel(
    private val getThemeUseCase: GetThemeUseCase,
    private val updateThemeUseCase: UpdateThemeUseCase
) : ViewModel() {

    private val _themeViewModel = MutableLiveData<Boolean>()
    val themeViewModel: LiveData<Boolean>
        get() = _themeViewModel

    fun setTheme() {
        _themeViewModel.value = getThemeUseCase()
    }

    fun updateTheme(isNight: Boolean) {
        updateThemeUseCase(isNight)
    }
}