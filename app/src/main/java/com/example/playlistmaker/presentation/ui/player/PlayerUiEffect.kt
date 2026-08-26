package com.example.playlistmaker.presentation.ui.player

sealed interface PlayerUiEffect {
    data object ClosePlayer : PlayerUiEffect
}