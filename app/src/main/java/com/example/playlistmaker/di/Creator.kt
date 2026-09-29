package com.example.playlistmaker.di

import android.app.Application
import com.example.playlistmaker.data.dto.local.TrackEntity
import com.example.playlistmaker.data.local.PrefsStorageClient
import com.example.playlistmaker.data.remote.NetworkClient
import com.example.playlistmaker.data.remote.NetworkFactory
import com.example.playlistmaker.data.repositories.LocalThemeRepositoryImpl
import com.example.playlistmaker.data.repositories.RemoteTrackRepositoryImpl
import com.example.playlistmaker.data.repositories.SearchHistoryRepositoryImpl
import com.example.playlistmaker.domain.repository.LocalThemeRepository
import com.example.playlistmaker.domain.repository.SearchHistoryRepository
import com.example.playlistmaker.domain.repository.TrackRepository
import com.example.playlistmaker.domain.usecases.AddTrackToSearchHistoryUseCase
import com.example.playlistmaker.domain.usecases.ClearHistoryUseCase
import com.example.playlistmaker.domain.usecases.GetHistoryListUseCase
import com.example.playlistmaker.domain.usecases.GetThemeUseCase
import com.example.playlistmaker.domain.usecases.GetTrackListUseCase
import com.example.playlistmaker.domain.usecases.UpdateThemeUseCase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Creator {

    private lateinit var application: Application
    private val gson = Gson()

    // init function
    fun init(application: Application) {
        this.application = application
    }

    private fun getNetworkClient(): NetworkClient {
        return NetworkFactory.apiService
    }

    // realizations of repositories
    private val searchHistoryRepository: SearchHistoryRepository by lazy {
        SearchHistoryRepositoryImpl(
            PrefsStorageClient(
                application,
                PrefsStorageClient.SHARED_KEY_CACHE,
                object : TypeToken<ArrayList<TrackEntity>>() {}.type,
                gson
            )
        )
    }

    private val trackRepository: TrackRepository by lazy {
        RemoteTrackRepositoryImpl(getNetworkClient())
    }

    private val themeRepository: LocalThemeRepository by lazy {
        LocalThemeRepositoryImpl(
            application,
            PrefsStorageClient(
                application,
                PrefsStorageClient.SHARED_KEY_THEME,
                object : TypeToken<Boolean>() {}.type,
                gson
            )
        )
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