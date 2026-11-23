package com.example.playlistmaker.di.modules

import com.example.playlistmaker.data.repositories.FavouriteTracksRepositoryImpl
import com.example.playlistmaker.data.repositories.LocalThemeRepositoryImpl
import com.example.playlistmaker.data.repositories.RemoteTrackRepositoryImpl
import com.example.playlistmaker.data.repositories.SearchHistoryRepositoryImpl
import com.example.playlistmaker.di.annotations.ApplicationScope
import com.example.playlistmaker.domain.repository.FavouriteTracksRepository
import com.example.playlistmaker.domain.repository.LocalThemeRepository
import com.example.playlistmaker.domain.repository.SearchHistoryRepository
import com.example.playlistmaker.domain.repository.TrackRepository
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {

    @Binds
    @ApplicationScope
    fun bindLocalThemeRepository(impl: LocalThemeRepositoryImpl): LocalThemeRepository

    @Binds
    @ApplicationScope
    fun bindSearchHistoryRepository(impl: SearchHistoryRepositoryImpl): SearchHistoryRepository

    @Binds
    @ApplicationScope
    fun bindTrackRepository(impl: RemoteTrackRepositoryImpl): TrackRepository

    @Binds
    @ApplicationScope
    fun bindFavouriteTrackRepository(impl: FavouriteTracksRepositoryImpl): FavouriteTracksRepository
}