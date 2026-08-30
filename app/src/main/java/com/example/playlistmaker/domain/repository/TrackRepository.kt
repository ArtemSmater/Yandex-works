package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.entities.Track
import io.reactivex.Single

interface TrackRepository {
    fun getTrackList(query: String): Single<List<Track>>
    fun getTrackList(): List<Track>
    fun fillCacheList(tracks: List<Track>)
}