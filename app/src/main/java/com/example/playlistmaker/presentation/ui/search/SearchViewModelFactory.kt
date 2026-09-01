package com.example.playlistmaker.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.domain.usecases.AddTrackToSearchHistoryUseCase
import com.example.playlistmaker.domain.usecases.ClearHistoryUseCase
import com.example.playlistmaker.domain.usecases.GetHistoryListUseCase
import com.example.playlistmaker.domain.usecases.GetTrackListUseCase

class SearchViewModelFactory(
    private val trackListUseCase: GetTrackListUseCase,
    private val cacheListUseCase: GetHistoryListUseCase,
    private val addTrackToSearchHistoryUseCase: AddTrackToSearchHistoryUseCase,
    private val clearHistoryUseCase: ClearHistoryUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(
            trackListUseCase,
            cacheListUseCase,
            addTrackToSearchHistoryUseCase,
            clearHistoryUseCase
        ) as T
    }
}