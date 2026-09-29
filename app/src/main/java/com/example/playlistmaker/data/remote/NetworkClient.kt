package com.example.playlistmaker.data.remote

import com.example.playlistmaker.data.dto.network.TrackRequest
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkClient {

    @GET("/search?entity=song")
    suspend fun getSongs(
        @Query(QUERY_PARAM_TERM) term: String,
        @Query(QUERY_PARAM_ENTITY) entity: String = "song"
    ): TrackRequest

    companion object {
        private const val QUERY_PARAM_ENTITY = "entity"
        private const val QUERY_PARAM_TERM = "term"
    }
}