package com.example.playlistmaker.presentation.ui.settings

sealed interface SettingsUiEffects {
    data object BackPressed : SettingsUiEffects
    data object IntentShare : SettingsUiEffects
    data object IntentSupport : SettingsUiEffects
    data object IntentAgreement : SettingsUiEffects
}