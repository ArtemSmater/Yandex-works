package com.example.playlistmaker.interfaces

import com.example.playlistmaker.pojo.Track

interface CacheSubscriber {
    fun getCacheTracks(list: MutableList<Track>)
}