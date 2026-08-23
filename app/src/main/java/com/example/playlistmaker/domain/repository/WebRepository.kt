package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.entities.Track
import io.reactivex.Single

interface WebRepository {
    fun getTrackList(entity: String) : Single<List<Track>>
}