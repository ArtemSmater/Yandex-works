package com.example.playlistmaker.data.mapper

import com.example.playlistmaker.data.dto.local.TrackEntity
import com.example.playlistmaker.data.dto.network.TrackDto
import com.example.playlistmaker.domain.entities.Track


fun TrackDto.mapDtoToDomain(): Track {
    return Track(
        trackId,
        previewUrl,
        trackName,
        artistName,
        trackTimeMillis,
        artworkUrl100,
        collectionName,
        releaseDate,
        primaryGenreName,
        country
    )
}

fun Track.mapDomainToEntity(): TrackEntity {
    return TrackEntity(
        trackId,
        previewUrl,
        trackName,
        artistName,
        trackTimeMillis,
        artworkUrl100,
        collectionName,
        releaseDate,
        primaryGenreName,
        country
    )
}

fun TrackEntity.mapEntityToDomain(): Track {
    return Track(
        trackId,
        previewUrl,
        trackName,
        artistName,
        trackTimeMillis,
        artworkUrl100,
        collectionName,
        releaseDate,
        primaryGenreName,
        country
    )
}

fun List<Track>.mapDomainListToEntityList(): List<TrackEntity> = map {
    it.mapDomainToEntity()
}

fun List<TrackDto>.mapDtoListToDomainList(): List<Track> = map {
    it.mapDtoToDomain()
}

fun List<TrackEntity>.mapEntityListToDomainList(): List<Track> = map {
    it.mapEntityToDomain()
}
