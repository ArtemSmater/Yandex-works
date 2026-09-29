package com.example.playlistmaker.presentation.ui.settings

sealed interface SettingsUiActions {

    object ShareAction : SettingsUiActions
    object SupportAction : SettingsUiActions
    object CheckAgreementAction : SettingsUiActions
    object BackPressed : SettingsUiActions
    class UpdateTheme(val isChecked: Boolean) : SettingsUiActions
}