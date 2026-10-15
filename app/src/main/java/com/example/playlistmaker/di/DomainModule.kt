package com.example.playlistmaker.di

import com.example.playlistmaker.data.repositories.LocalThemeRepositoryImpl
import com.example.playlistmaker.data.repositories.RemoteTrackRepositoryImpl
import com.example.playlistmaker.data.repositories.SearchHistoryRepositoryImpl
import com.example.playlistmaker.domain.repository.LocalThemeRepository
import com.example.playlistmaker.domain.repository.SearchHistoryRepository
import com.example.playlistmaker.domain.repository.TrackRepository
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {

    @Binds
    fun bindLocalThemeRepository(impl: LocalThemeRepositoryImpl): LocalThemeRepository

    @Binds
    fun bindSearchHistoryRepository(impl: SearchHistoryRepositoryImpl): SearchHistoryRepository

    @Binds
    fun bindTrackRepository(impl: RemoteTrackRepositoryImpl): TrackRepository
}