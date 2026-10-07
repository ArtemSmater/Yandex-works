package com.example.playlistmaker.presentation.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.usecases.GetThemeUseCase
import com.example.playlistmaker.domain.usecases.UpdateThemeUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(
    private val getThemeUseCase: GetThemeUseCase,
    private val updateThemeUseCase: UpdateThemeUseCase
) : ViewModel() {

    private val _themeViewModel = MutableStateFlow(getThemeUseCase())
    val themeViewModel = _themeViewModel.asStateFlow()

    private var _settingsViewModelEffects =
        MutableSharedFlow<SettingsUiEffects>(extraBufferCapacity = 1)
    val settingsViewModelEffects = _settingsViewModelEffects.asSharedFlow()


    fun uiAction(action: SettingsUiActions) {
        when (action) {
            is SettingsUiActions.ShareAction -> {
                getShareAction()
            }

            is SettingsUiActions.SupportAction -> {
                getSupportAction()
            }

            is SettingsUiActions.CheckAgreementAction -> {
                getCheckAction()
            }

            is SettingsUiActions.BackPressed -> {
                getBackPressedAction()
            }

            is SettingsUiActions.UpdateTheme -> {
                updateTheme(action.isChecked)
            }
        }
    }

    private fun getShareAction() {
        _settingsViewModelEffects.tryEmit(SettingsUiEffects.IntentShare)
    }

    private fun getSupportAction() {
        _settingsViewModelEffects.tryEmit(SettingsUiEffects.IntentSupport)
    }

    private fun getCheckAction() {
        _settingsViewModelEffects.tryEmit(SettingsUiEffects.IntentAgreement)
    }

    private fun getBackPressedAction() {
        _settingsViewModelEffects.tryEmit(SettingsUiEffects.BackPressed)
    }

    private fun updateTheme(isChecked: Boolean) {
        updateThemeUseCase(isChecked)
        _themeViewModel.value = getThemeUseCase()
    }

    companion object {
        fun getFactory(
            getThemeUseCase: GetThemeUseCase,
            updateThemeUseCase: UpdateThemeUseCase
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SettingsViewModel(getThemeUseCase, updateThemeUseCase)
                }
            }
    }
}