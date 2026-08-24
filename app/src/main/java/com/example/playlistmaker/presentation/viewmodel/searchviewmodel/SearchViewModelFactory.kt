package com.example.playlistmaker.presentation.viewmodel.searchviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.domain.repository.CacheRepository
import com.example.playlistmaker.domain.repository.WebRepository
import com.example.playlistmaker.domain.usecases.UpdateCacheUseCase
import com.example.playlistmaker.domain.usecases.GetCacheListUseCase
import com.example.playlistmaker.domain.usecases.GetTrackListUseCase

class SearchViewModelFactory(
    private val cacheRepository: CacheRepository,
    private val webRepository: WebRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(
            GetTrackListUseCase(webRepository),
            GetCacheListUseCase(cacheRepository),
            UpdateCacheUseCase(cacheRepository)
        ) as T
    }
}