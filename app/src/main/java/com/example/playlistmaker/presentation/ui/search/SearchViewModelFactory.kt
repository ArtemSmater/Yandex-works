package com.example.playlistmaker.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.domain.usecases.GetCacheListUseCase
import com.example.playlistmaker.domain.usecases.GetTrackListUseCase
import com.example.playlistmaker.domain.usecases.UpdateCacheUseCase

class SearchViewModelFactory(
    private val trackListUseCase: GetTrackListUseCase,
    private val cacheListUseCase: GetCacheListUseCase,
    private val updateCacheUseCase: UpdateCacheUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(
            trackListUseCase,
            cacheListUseCase,
            updateCacheUseCase
        ) as T
    }
}