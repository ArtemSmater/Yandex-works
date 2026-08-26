package com.example.playlistmaker.data.repositories

import com.example.playlistmaker.data.network.ApiService
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.repository.WebRepository
import io.reactivex.Single

class WebRepositoryImpl(
    private val apiService: ApiService
) : WebRepository {

    override fun getTrackList(
        entity: String
    ): Single<List<Track>> {
        return apiService
            .getSongs(entity)
            .map { it.results }
    }
}