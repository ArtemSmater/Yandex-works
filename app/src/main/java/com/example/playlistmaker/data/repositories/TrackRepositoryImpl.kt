package com.example.playlistmaker.data.repositories

import com.example.playlistmaker.data.mapper.TrackMapper
import com.example.playlistmaker.data.network.ApiService
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.repository.TrackRepository
import io.reactivex.Single

class TrackRepositoryImpl(
    private val service: ApiService
) : TrackRepository {

    private val mapper = TrackMapper()

    override fun getTrackList(query: String): Single<List<Track>> {
        return service.getSongs(query).map { mapper.mapDtoListToEntityList(it.results)}
    }
}