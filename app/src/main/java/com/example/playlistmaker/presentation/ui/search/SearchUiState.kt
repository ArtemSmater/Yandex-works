package com.example.playlistmaker.presentation.ui.search

import com.example.playlistmaker.domain.entities.Track

sealed class SearchUiState {

    object Initial : SearchUiState()
    object Loading : SearchUiState()
    object Empty : SearchUiState()
    object WebError : SearchUiState()
    class WebTracks(val tracks: List<Track>) : SearchUiState()
    class CacheTracks(val tracks: List<Track>) : SearchUiState()
}