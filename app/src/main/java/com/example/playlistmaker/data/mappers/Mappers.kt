package com.example.playlistmaker.data.mappers

import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.entities.Track

class Mappers {
    fun mapDtoToEntity(track: TrackDto): Track {
        return Track(
            track.trackId,
            track.previewUrl,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country
        )
    }

    fun mapEntityToDto(track: Track): TrackDto {
        return TrackDto(
            track.trackId,
            track.previewUrl,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country
        )
    }

    fun mapEntityListToDtoList(list: List<Track>) = list.map {
        mapEntityToDto(it)
    }
}