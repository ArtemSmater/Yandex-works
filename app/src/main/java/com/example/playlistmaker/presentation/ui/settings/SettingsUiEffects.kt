package com.example.playlistmaker.presentation.ui.settings

sealed interface SettingsUiEffects {
    object BackPressed : SettingsUiEffects
    object IntentShare : SettingsUiEffects
    object IntentSupport : SettingsUiEffects
    object IntentAgreement : SettingsUiEffects
}