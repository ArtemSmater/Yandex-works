package com.example.playlistmaker.data.local.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("saved_tracks")
data class TrackEntity(
    @PrimaryKey
    val trackId: String,
    val previewUrl: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String
)