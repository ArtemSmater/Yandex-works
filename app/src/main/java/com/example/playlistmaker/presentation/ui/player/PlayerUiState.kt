package com.example.playlistmaker.presentation.ui.player

sealed class PlayerUiState {

    object Initial : PlayerUiState()
    object Prepared : PlayerUiState()
    class Playing(val progress: String) : PlayerUiState()
    class Paused(val progress: String) : PlayerUiState()
}