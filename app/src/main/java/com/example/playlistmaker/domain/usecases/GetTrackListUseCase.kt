package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.entities.TrackRequest
import com.example.playlistmaker.domain.repository.WebRepository
import io.reactivex.Single

class GetTrackListUseCase(private val repository: WebRepository) {
    operator fun invoke(entity: String): Single<TrackRequest> {
        return repository.getTrackList(entity)
    }
}