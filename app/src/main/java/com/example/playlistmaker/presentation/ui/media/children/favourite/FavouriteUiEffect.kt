package com.example.playlistmaker.presentation.ui.media.children.favourite

import com.example.playlistmaker.domain.entities.Track

sealed interface FavouriteUiEffect {
    class OpenPlayer(val track: Track) : FavouriteUiEffect
}