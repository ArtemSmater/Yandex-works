package com.example.playlistmaker.domain.repository

interface ThemeRepository {
    fun getThemeValue() : Boolean
    fun setThemeValue(isNight: Boolean)
}