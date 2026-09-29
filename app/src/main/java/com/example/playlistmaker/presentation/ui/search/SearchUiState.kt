package com.example.playlistmaker.presentation.ui.search

import com.example.playlistmaker.domain.entities.Track

sealed interface SearchUiState {

    object Initial : SearchUiState
    object Loading : SearchUiState
    class Error(val error: SearchFragmentErrors) : SearchUiState
    class WebTracks(val tracks: List<Track>) : SearchUiState
    class HistoryTracks(val tracks: List<Track>) : SearchUiState
}