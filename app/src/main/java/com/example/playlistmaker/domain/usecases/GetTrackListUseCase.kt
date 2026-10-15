package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.repository.TrackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetTrackListUseCase @Inject constructor(
    private val repository: TrackRepository
) {
    suspend operator fun invoke(query: String): List<Track> {
        return withContext(Dispatchers.IO) {
            repository.getTrackList(query)
        }
    }
}