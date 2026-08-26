package com.example.playlistmaker.presentation.ui.search

import com.example.playlistmaker.domain.entities.Track

sealed interface SearchUiState {

    data object Initial : SearchUiState
    data object Loading : SearchUiState
    data object Empty : SearchUiState
    data object WebError : SearchUiState
    data class WebTracks(val tracks: List<Track>) : SearchUiState
    data class CacheTracks(val tracks: List<Track>) : SearchUiState
}