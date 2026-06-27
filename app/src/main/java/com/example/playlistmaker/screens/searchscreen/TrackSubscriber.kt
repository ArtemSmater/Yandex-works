package com.example.playlistmaker.screens.searchscreen

import com.example.playlistmaker.pojo.Track

fun interface TrackSubscriber {
    fun trackList(tracks: List<Track>)
}