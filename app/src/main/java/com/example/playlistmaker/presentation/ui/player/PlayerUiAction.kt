package com.example.playlistmaker.presentation.ui.player

sealed interface PlayerUiAction {

    object Play : PlayerUiAction
    object Pause : PlayerUiAction
    object Back : PlayerUiAction
}