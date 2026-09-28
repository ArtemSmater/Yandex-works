package com.example.playlistmaker.presentation.ui.player

sealed interface PlayerUiEffect {
    object ClosePlayer : PlayerUiEffect
}