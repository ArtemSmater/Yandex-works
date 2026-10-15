package com.example.playlistmaker.data.repositories

import com.example.playlistmaker.data.mapper.mapDtoListToDomainList
import com.example.playlistmaker.data.remote.NetworkClient
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.repository.TrackRepository
import javax.inject.Inject

class RemoteTrackRepositoryImpl @Inject constructor(
    private val service: NetworkClient
) : TrackRepository {

    override suspend fun getTrackList(query: String): List<Track> {
        return service.getSongs(query).results.orEmpty().mapDtoListToDomainList()
    }
}