package com.example.playlistmaker.interfaces

import com.example.playlistmaker.pojo.Track

fun interface TrackSubscriber {
    fun trackList(tracks: List<Track>)
}