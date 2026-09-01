package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.TrackRequest
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/search?entity=song")
    fun getSongs(
        @Query(QUERY_PARAM_TERM) term: String,
        @Query(QUERY_PARAM_ENTITY) entity: String = "song"
    ): Single<TrackRequest>

    companion object {
        private const val QUERY_PARAM_ENTITY = "entity"
        private const val QUERY_PARAM_TERM = "term"
    }
}