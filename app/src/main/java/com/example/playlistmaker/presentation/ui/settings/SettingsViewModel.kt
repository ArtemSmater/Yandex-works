package com.example.playlistmaker.presentation.ui.settings

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.App
import com.example.playlistmaker.domain.usecases.GetThemeUseCase
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class SettingsViewModel(
    private val getThemeUseCase: GetThemeUseCase,
    private val application: Application
) : ViewModel() {

    private val _themeViewModel = BehaviorSubject.create<Boolean>()
    val themeViewModel: Observable<Boolean> = _themeViewModel.hide()

    private var _settingsViewModelEffects = PublishSubject.create<SettingsUiEffects>()
    val settingsViewModelEffects: Observable<SettingsUiEffects> = _settingsViewModelEffects.hide()

    init {
        _themeViewModel.onNext(getThemeUseCase())
    }

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
        _settingsViewModelEffects.onNext(SettingsUiEffects.IntentShare)
    }

    private fun getSupportAction() {
        _settingsViewModelEffects.onNext(SettingsUiEffects.IntentSupport)
    }

    private fun getCheckAction() {
        _settingsViewModelEffects.onNext(SettingsUiEffects.IntentAgreement)
    }

    private fun getBackPressedAction() {
        _settingsViewModelEffects.onNext(SettingsUiEffects.BackPressed)
    }

    private fun updateTheme(isChecked: Boolean) {
        (application as App).switchTheme(isChecked)
        _themeViewModel.onNext(getThemeUseCase())
    }

    companion object {
        fun getFactory(getThemeUseCase: GetThemeUseCase): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val app = (this[APPLICATION_KEY] as Application)
                    SettingsViewModel(getThemeUseCase, app)
                }
            }
    }
}