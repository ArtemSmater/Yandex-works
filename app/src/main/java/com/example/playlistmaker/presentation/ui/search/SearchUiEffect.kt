package com.example.playlistmaker.presentation.ui.search

import com.example.playlistmaker.domain.entities.Track

sealed interface SearchUiEffect {
    data object BackPressed : SearchUiEffect
    data class OpenPlayer(val track: Track) : SearchUiEffect
}