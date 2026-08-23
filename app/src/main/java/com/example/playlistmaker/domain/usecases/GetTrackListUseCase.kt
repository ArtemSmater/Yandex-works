package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.repository.WebRepository
import io.reactivex.Single

class GetTrackListUseCase(private val repository: WebRepository) {
    operator fun invoke(entity: String): Single<List<Track>> {
        return repository.getTrackList(entity)
    }
}