package com.example.playlistmaker.presentation.ui.player

sealed class PlayerUiState(
    val isPlayEnabled: Boolean,
    val isPlaying: Boolean,
    val progress: String
) {

    object Initial : PlayerUiState(false, false, "0:00")
    object Prepared : PlayerUiState(true, false, "0:00")
    class Playing(progress: String) : PlayerUiState(true, true, progress)
    class Paused(progress: String) : PlayerUiState(true, false, progress)
    class Error(message: String) : PlayerUiState(false, false, message)
}