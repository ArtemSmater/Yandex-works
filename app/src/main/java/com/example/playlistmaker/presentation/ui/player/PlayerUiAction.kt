package com.example.playlistmaker.presentation.ui.player

sealed class PlayerUiAction {

    object Play : PlayerUiAction()
    object Release : PlayerUiAction()
    object Back : PlayerUiAction()
}