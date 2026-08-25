package com.example.playlistmaker.presentation.ui.player

sealed class PlayerUiAction {

    object Play : PlayerUiAction()
    object Pause : PlayerUiAction()
    object Back : PlayerUiAction()
}