package com.example.playlistmaker.presentation.ui.player

sealed interface PlayerUiAction {

    data object Play : PlayerUiAction
    data object Pause : PlayerUiAction
    data object Back : PlayerUiAction
}