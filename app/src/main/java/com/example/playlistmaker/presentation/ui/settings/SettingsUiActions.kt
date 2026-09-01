package com.example.playlistmaker.presentation.ui.settings

sealed interface SettingsUiActions {

    data object ShareAction : SettingsUiActions
    data object SupportAction : SettingsUiActions
    data object CheckAgreementAction: SettingsUiActions
    data object BackPressed : SettingsUiActions
    data class UpdateTheme(val isChecked: Boolean) : SettingsUiActions
}