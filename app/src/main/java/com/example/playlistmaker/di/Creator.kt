package com.example.playlistmaker.di

import android.app.Application
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

    private lateinit var application: Application
    private var localStorage: PreferenceStorage? = null
    private val LOCK = Any()

    private fun getNetworkService(): ApiService {
        return ApiFactory.apiService
    }

    private fun getStorageService(application: Application): PreferenceStorage {
        localStorage?.let {
            return it
        }
        synchronized(LOCK) {
            localStorage?.let {
                return it
            }

            val storage = PreferenceStorage(application)
            localStorage = storage
            return storage
        }
    }

    private val trackRepository: TrackRepository by lazy {
        TrackRepositoryImpl(getStorageService(application), getNetworkService())
    }

    private val themeRepository: ThemeRepository by lazy {
        ThemeRepositoryImpl(getStorageService(application))
    }

    // init function
    fun init(application: Application) {
        this.application = application
    }

    // get use cases
    val getCacheListUseCase: GetCacheListUseCase by lazy {
        GetCacheListUseCase(trackRepository)
    }

    val getTrackListUseCase: GetTrackListUseCase by lazy {
        GetTrackListUseCase(trackRepository)
    }

    val getThemeUseCase: GetThemeUseCase by lazy {
        GetThemeUseCase(themeRepository)
    }

    val getUpdateCacheUseCase: UpdateCacheUseCase by lazy {
        UpdateCacheUseCase(trackRepository)
    }

    val getUpdateThemeUseCase: UpdateThemeUseCase by lazy {
        UpdateThemeUseCase(themeRepository)
    }
}