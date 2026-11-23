package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.entities.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteTracksRepository {

    fun getFavouriteTracks(): Flow<List<Track>>
    fun checkFavouriteTrack(trackId: String): Flow<Boolean>
    suspend fun toggleFavourite(track: Track)
}