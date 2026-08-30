package com.example.playlistmaker.data.repositories

import com.example.playlistmaker.data.mappers.Mappers
import com.example.playlistmaker.data.localcache.PreferenceStorage
import com.example.playlistmaker.data.network.ApiService
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.repository.TrackRepository
import io.reactivex.Single

class TrackRepositoryImpl(
    private val storage: PreferenceStorage,
    private val service: ApiService
) : TrackRepository {

    private val mapper = Mappers()

    override fun getTrackList(query: String): Single<List<Track>> {
        return service.getSongs(query).map { it.results }
    }

    override fun getTrackList(): List<Track> {
        return storage.getCache().map { mapper.mapDtoToEntity(it) }
    }

    override fun fillCacheList(tracks: List<Track>) {
        storage.fillCache(mapper.mapEntityListToDtoList(tracks))
    }
}