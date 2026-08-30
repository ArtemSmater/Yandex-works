package com.example.playlistmaker.presentation.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.App
import com.example.playlistmaker.domain.usecases.GetThemeUseCase
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class SettingsViewModel(
    private val getThemeUseCase: GetThemeUseCase,
    private val application: Application
) : AndroidViewModel(application) {

    private val _themeViewModel = MutableLiveData<Boolean>()
    val themeViewModel: LiveData<Boolean>
        get() = _themeViewModel

    private var _settingsViewModelEffects = PublishSubject.create<SettingsUiEffects>()
    val settingsViewModelEffects: Observable<SettingsUiEffects> = _settingsViewModelEffects.hide()

    init {
        _themeViewModel.value = getThemeUseCase()
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
        _themeViewModel.value = getThemeUseCase()
    }
}