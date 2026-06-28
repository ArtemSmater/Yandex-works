package com.example.playlistmaker.api

import com.example.playlistmaker.pojo.TracksRequest
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/search?entity=song")
    fun getSongs(
        @Query(QUERY_PARAM_TERM) term: String,
        @Query(QUERY_PARAM_ENTITY) entity: String = "song"
    ): Single<TracksRequest>

    companion object {
        private const val QUERY_PARAM_ENTITY = "entity"
        private const val QUERY_PARAM_TERM = "term"
    }
}