package com.example.playlistmaker.presentation.ui.search

import com.example.playlistmaker.domain.entities.Track

sealed class SearchUiEffect {
    object BackPressed : SearchUiEffect()
    class OpenPlayer(val track: Track) : SearchUiEffect()
}