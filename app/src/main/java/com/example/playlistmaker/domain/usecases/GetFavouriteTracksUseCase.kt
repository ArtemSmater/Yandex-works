package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.repository.FavouriteTracksRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavouriteTracksUseCase @Inject constructor(
    private val repository: FavouriteTracksRepository
) {
    operator fun invoke(): Flow<List<Track>> {
        return repository.getFavouriteTracks()
    }
}