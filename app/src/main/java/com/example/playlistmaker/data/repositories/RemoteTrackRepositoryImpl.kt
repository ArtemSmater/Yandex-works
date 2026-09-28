package com.example.playlistmaker.data.repositories

import com.example.playlistmaker.data.mapper.mapDtoListToDomainList
import com.example.playlistmaker.data.remote.NetworkClient
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.repository.TrackRepository
import io.reactivex.Single

class RemoteTrackRepositoryImpl(
    private val service: NetworkClient
) : TrackRepository {

    override fun getTrackList(query: String): Single<List<Track>> {
        return service.getSongs(query).map { it.results?.mapDtoListToDomainList() }
    }
}