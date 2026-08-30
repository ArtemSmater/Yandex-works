package com.example.playlistmaker.data.dto

import com.example.playlistmaker.domain.entities.Track

data class TrackRequest(val results: List<Track>? = null)