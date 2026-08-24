package com.example.playlistmaker.presentation.ui.search

import com.example.playlistmaker.domain.entities.Track

sealed class SearchUiAction {

    object BackPressed : SearchUiAction()
    object ClearCache : SearchUiAction()
    object RetryQuery : SearchUiAction()
    class TrackClicked(val track: Track, val isCached: Boolean) : SearchUiAction()
    class FieldChanged(val focused: Boolean, val s: CharSequence?) : SearchUiAction()
}