package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.data.localcache.PreferenceStorage
import com.example.playlistmaker.data.network.ApiFactory
import com.example.playlistmaker.data.network.ApiService
import com.example.playlistmaker.data.repositories.ThemeRepositoryImpl
import com.example.playlistmaker.data.repositories.TrackRepositoryImpl
import com.example.playlistmaker.domain.repository.ThemeRepository
import com.example.playlistmaker.domain.repository.TrackRepository
import com.example.playlistmaker.domain.usecases.GetCacheListUseCase
import com.example.playlistmaker.domain.usecases.GetThemeUseCase
import com.example.playlistmaker.domain.usecases.GetTrackListUseCase
import com.example.playlistmaker.domain.usecases.UpdateCacheUseCase
import com.example.playlistmaker.domain.usecases.UpdateThemeUseCase

object Creator {

    private var localStorage: PreferenceStorage? = null
    private val LOCK = Any()

    private fun getNetworkService(): ApiService {
        return ApiFactory.apiService
    }

    private fun getStorageService(context: Context): PreferenceStorage {
        localStorage?.let {
            return it
        }
        synchronized(LOCK) {
            localStorage?.let {
                return it
            }

            val storage = PreferenceStorage(context)
            localStorage = storage
            return storage
        }
    }

    private fun getTrackRepository(context: Context): TrackRepository {
        return TrackRepositoryImpl(getStorageService(context), getNetworkService())
    }

    private fun getThemeRepository(context: Context): ThemeRepository {
        return ThemeRepositoryImpl(getStorageService(context))
    }

    // get use cases
    fun provideCacheListUseCase(context: Context): GetCacheListUseCase {
        return GetCacheListUseCase(getTrackRepository(context))
    }

    fun provideTrackListUseCase(context: Context): GetTrackListUseCase {
        return GetTrackListUseCase(getTrackRepository(context))
    }

    fun provideThemeUseCase(context: Context): GetThemeUseCase {
        return GetThemeUseCase(getThemeRepository(context))
    }

    fun provideUpdateCacheUseCase(context: Context): UpdateCacheUseCase {
        return UpdateCacheUseCase(getTrackRepository(context))
    }

    fun provideUpdateThemeUseCase(context: Context): UpdateThemeUseCase {
        return UpdateThemeUseCase(getThemeRepository(context))
    }
}