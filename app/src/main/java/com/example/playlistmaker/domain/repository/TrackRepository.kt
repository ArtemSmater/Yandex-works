package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.entities.Track

interface TrackRepository {
    suspend fun getTrackList(query: String): List<Track>
}