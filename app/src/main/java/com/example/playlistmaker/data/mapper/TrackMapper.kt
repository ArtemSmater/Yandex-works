package com.example.playlistmaker.data.mapper

import com.example.playlistmaker.data.dto.local.TrackEntity
import com.example.playlistmaker.data.dto.network.TrackDto
import com.example.playlistmaker.domain.entities.Track


fun TrackDto.mapDtoToDomain(): Track {
    return Track(
        getEmptyString(trackId),
        getEmptyString(previewUrl),
        getEmptyString(trackName),
        getEmptyString(artistName),
        trackTimeMillis?: 0L,
        getHighQualityLink(getEmptyString(artworkUrl100)),
        getEmptyString(collectionName),
        getYear(releaseDate),
        getEmptyString(primaryGenreName),
        getEmptyString(country)
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

private fun getYear(date: String?): String {
    date?.let {
        return it.substringBefore("-")
    }
    return ""
}

private fun getHighQualityLink(baseLink: String): String {
    return if (baseLink.contains(LOW_QUALITY_ENDPOINT)) {
        baseLink.replace(LOW_QUALITY_ENDPOINT, HIGH_QUALITY_ENDPOINT)
    } else {
        baseLink
    }
}

private fun getEmptyString(line: String?): String {
    line?.let {
        return it
    }
    return ""
}

private const val HIGH_QUALITY_ENDPOINT = "512x512bb.jpg"
private const val LOW_QUALITY_ENDPOINT = "100x100bb.jpg"
