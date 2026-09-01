package com.example.playlistmaker.presentation.ui.search

import com.example.playlistmaker.domain.entities.Track

sealed interface SearchUiAction {

    data object BackPressed : SearchUiAction
    data object ClearHistory : SearchUiAction
    data object RetryQuery : SearchUiAction
    data class TrackClicked(val track: Track, val isCached: Boolean) : SearchUiAction
    data class FieldChanged(val focused: Boolean, val s: CharSequence?) : SearchUiAction
}