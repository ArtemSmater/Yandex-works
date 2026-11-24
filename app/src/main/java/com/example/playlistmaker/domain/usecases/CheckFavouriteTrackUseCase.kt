package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.repository.FavouriteTracksRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckFavouriteTrackUseCase @Inject constructor(
    private val repository: FavouriteTracksRepository
) {
    operator fun invoke(trackId: String): Flow<Boolean> {
        return repository.checkFavouriteTrack(trackId)
    }
}