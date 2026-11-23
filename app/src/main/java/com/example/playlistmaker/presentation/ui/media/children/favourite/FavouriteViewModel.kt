package com.example.playlistmaker.presentation.ui.media.children.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.usecases.GetFavouriteTracksUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class FavouriteViewModel @Inject constructor(
    getFavouriteTracksUseCase: GetFavouriteTracksUseCase
) : ViewModel() {

    val state = getFavouriteTracksUseCase()
        .map { tracks ->
            if (tracks.isEmpty()) {
                FavouriteUiState.Empty
            } else {
                FavouriteUiState.Content(tracks)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FavouriteUiState.Content(emptyList())
        )
    private val _effect = MutableSharedFlow<FavouriteUiEffect>(extraBufferCapacity = 1)
    val effect = _effect.asSharedFlow()

    fun action(action: FavouriteUiActions) {
        when (action) {
            is FavouriteUiActions.TrackClicked -> {
                _effect.tryEmit(FavouriteUiEffect.OpenPlayer(action.track))
            }
        }
    }
}