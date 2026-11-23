package com.example.playlistmaker.presentation.ui.media.children.favourite

import com.example.playlistmaker.domain.entities.Track

sealed interface FavouriteUiActions {
    class TrackClicked(val track: Track): FavouriteUiActions
}