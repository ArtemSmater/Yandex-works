package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.repository.FavouriteTracksRepository
import javax.inject.Inject

class ToggleFavouriteUseCase @Inject constructor(
    private val repository: FavouriteTracksRepository
) {
    suspend operator fun invoke(track: Track) {
        repository.toggleFavourite(track)
    }
}