package com.example.playlistmaker.di

import android.app.Application
import com.example.playlistmaker.data.localcache.PreferenceStorage
import com.example.playlistmaker.data.localcache.SearchHistoryStorage
import com.example.playlistmaker.data.localcache.ThemeValueStorage
import com.example.playlistmaker.data.network.ApiFactory
import com.example.playlistmaker.data.network.ApiService
import com.example.playlistmaker.data.repositories.HistoryRepositoryImpl
import com.example.playlistmaker.data.repositories.ThemeRepositoryImpl
import com.example.playlistmaker.data.repositories.TrackRepositoryImpl
import com.example.playlistmaker.domain.repository.HistoryRepository
import com.example.playlistmaker.domain.repository.ThemeRepository
import com.example.playlistmaker.domain.repository.TrackRepository
import com.example.playlistmaker.domain.usecases.AddTrackToSearchHistoryUseCase
import com.example.playlistmaker.domain.usecases.ClearHistoryUseCase
import com.example.playlistmaker.domain.usecases.GetHistoryListUseCase
import com.example.playlistmaker.domain.usecases.GetThemeUseCase
import com.example.playlistmaker.domain.usecases.GetTrackListUseCase
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

    // local storages
    private val themeValueStorage: ThemeValueStorage by lazy {
        ThemeValueStorage(getStorageService(application))
    }

    private val searchHistoryStorage: SearchHistoryStorage by lazy {
        SearchHistoryStorage(getStorageService(application))
    }

    // realizations of repositories
    private val searchHistoryRepository: HistoryRepository by lazy {
        HistoryRepositoryImpl(searchHistoryStorage)
    }

    private val trackRepository: TrackRepository by lazy {
        TrackRepositoryImpl(getNetworkService())
    }

    private val themeRepository: ThemeRepository by lazy {
        ThemeRepositoryImpl(themeValueStorage)
    }

    // init function
    fun init(application: Application) {
        this.application = application
    }

    // get use cases
    val getHistoryListUseCase: GetHistoryListUseCase by lazy {
        GetHistoryListUseCase(searchHistoryRepository)
    }

    val getTrackListUseCase: GetTrackListUseCase by lazy {
        GetTrackListUseCase(trackRepository)
    }

    val getThemeUseCase: GetThemeUseCase by lazy {
        GetThemeUseCase(themeRepository)
    }

    val addTrackToSearchHistoryUseCase: AddTrackToSearchHistoryUseCase by lazy {
        AddTrackToSearchHistoryUseCase(searchHistoryRepository)
    }

    val getUpdateThemeUseCase: UpdateThemeUseCase by lazy {
        UpdateThemeUseCase(themeRepository)
    }

    val clearHistoryUseCase: ClearHistoryUseCase by lazy {
        ClearHistoryUseCase(searchHistoryRepository)
    }
}