package com.example.playlistmaker.presentation.ui.player

sealed interface PlayerUiState {

    data object Initial : PlayerUiState
    data object Prepared : PlayerUiState
    data class Playing(val progress: String) : PlayerUiState
    data class Paused(val progress: String) : PlayerUiState
}