package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.entities.TrackRequest
import io.reactivex.Single

interface WebRepository {
    fun getTrackList(entity: String) : Single<TrackRequest>
}