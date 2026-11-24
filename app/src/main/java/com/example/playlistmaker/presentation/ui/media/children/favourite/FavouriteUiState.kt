package com.example.playlistmaker.presentation.ui.media.children.favourite

import com.example.playlistmaker.domain.entities.Track

sealed interface FavouriteUiState {
    object Empty: FavouriteUiState
    class Content(val tracks: List<Track>): FavouriteUiState
}