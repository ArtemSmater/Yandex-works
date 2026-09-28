package com.example.playlistmaker.domain.repository

interface LocalThemeRepository {
    fun getThemeValue() : Boolean
    fun setThemeValue(isNight: Boolean)
}