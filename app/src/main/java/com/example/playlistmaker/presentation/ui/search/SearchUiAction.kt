package com.example.playlistmaker.presentation.ui.search

import com.example.playlistmaker.domain.entities.Track

sealed interface SearchUiAction {

    object BackPressed : SearchUiAction
    object ClearHistory : SearchUiAction
    object RetryQuery : SearchUiAction
    class TrackClicked(val track: Track) : SearchUiAction
    class FieldChanged(val focused: Boolean, val s: CharSequence?) : SearchUiAction
}