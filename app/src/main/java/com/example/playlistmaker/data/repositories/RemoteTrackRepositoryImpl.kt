package com.example.playlistmaker.data.repositories

import com.example.playlistmaker.data.mapper.mapDtoListToDomainList
import com.example.playlistmaker.data.remote.NetworkClient
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.repository.TrackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteTrackRepositoryImpl(
    private val service: NetworkClient
) : TrackRepository {

    override suspend fun getTrackList(query: String): List<Track> {
        return withContext(Dispatchers.IO) {
            service.getSongs(query).results?.mapDtoListToDomainList() ?: emptyList()
        }
    }
}