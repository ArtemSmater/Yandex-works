package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.repository.TrackRepository
import io.reactivex.Single

class GetTrackListUseCase(private val repository: TrackRepository) {
    operator fun invoke(query: String): Single<List<Track>> {
        return repository.getTrackList(query)
    }
}