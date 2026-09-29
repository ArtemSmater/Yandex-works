package com.example.playlistmaker.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.usecases.AddTrackToSearchHistoryUseCase
import com.example.playlistmaker.domain.usecases.ClearHistoryUseCase
import com.example.playlistmaker.domain.usecases.GetHistoryListUseCase
import com.example.playlistmaker.domain.usecases.GetTrackListUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class SearchViewModel(
    private val getTrackListUseCase: GetTrackListUseCase,
    private val getHistoryListUseCase: GetHistoryListUseCase,
    private val addTrackToSearchHistoryUseCase: AddTrackToSearchHistoryUseCase,
    private val clearHistoryUseCase: ClearHistoryUseCase
) : ViewModel() {

    private lateinit var lastQuery: String

    // main observable query field
    private val queryValue = MutableStateFlow("")

    // double click security
    private val trackClick = MutableSharedFlow<Track>(extraBufferCapacity = 1)

    // for ui state subscribers
    private val _searchViewModelState = MutableStateFlow<SearchUiState>(SearchUiState.Initial)
    val state = _searchViewModelState.asStateFlow()

    // for effects subscribers
    private val _searchViewModelEffect = MutableSharedFlow<SearchUiEffect>(extraBufferCapacity = 1)
    val effect = _searchViewModelEffect.asSharedFlow()

    init {
        getTrackList()
        getTrackClick()
    }

    fun uiAction(action: SearchUiAction) {
        when (action) {
            is SearchUiAction.ClearHistory -> {
                clearTrackHistory()
            }

            is SearchUiAction.RetryQuery -> {
                retryQuery()
            }

            is SearchUiAction.TrackClicked -> {
                trackClicked(action.track)
            }

            is SearchUiAction.FieldChanged -> {
                fieldChanged(action.s, action.focused)
            }

            is SearchUiAction.BackPressed -> {
                backPressed()
            }
        }
    }

    private fun backPressed() {
        _searchViewModelEffect.tryEmit(SearchUiEffect.BackPressed)
    }

    private fun fieldChanged(charSequence: CharSequence?, isFocused: Boolean) {
        viewModelScope.launch {
            if (charSequence == null) {
                _searchViewModelState.value = SearchUiState.Initial
                return@launch
            }

            if (charSequence.isEmpty() && isFocused && getHistoryListUseCase().isNotEmpty()) {
                _searchViewModelState.value = SearchUiState.HistoryTracks(getHistoryListUseCase())
                return@launch
            }

            if (_searchViewModelState.value is SearchUiState.HistoryTracks) {
                _searchViewModelState.value = SearchUiState.WebTracks(emptyList())
            }
            queryValue.value = charSequence.toString()
        }
    }

    private fun trackClicked(track: Track) {
        trackClick.tryEmit(track)
        viewModelScope.launch {
            addTrackToSearchHistoryUseCase(track)
        }
    }

    @OptIn(FlowPreview::class)
    private fun getTrackClick() {
        viewModelScope.launch {
            trackClick
                .debounce(200.milliseconds)
                .collect { _searchViewModelEffect.emit(SearchUiEffect.OpenPlayer(it)) }
        }
    }

    private fun retryQuery() {
        viewModelScope.launch {
            createSearchFlow(lastQuery).collect { _searchViewModelState.value = it }
        }
    }

    private fun clearTrackHistory() {
        _searchViewModelState.value = SearchUiState.HistoryTracks(emptyList())
        viewModelScope.launch {
            clearHistoryUseCase()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun getTrackList() {
        viewModelScope.launch {
            queryValue
                .debounce(2000.milliseconds)
                .map { it.trim() }
                .distinctUntilChanged()
                .flatMapLatest { createSearchFlow(it) }
                .collect { _searchViewModelState.value = it }
        }
    }

    private fun createSearchFlow(query: String): Flow<SearchUiState> {
        return createSearchRequest(query).takeWhile { query.length > 2 }
    }

    private fun createSearchRequest(query: String): Flow<SearchUiState> = flow {
        lastQuery = query
        emit(SearchUiState.Loading)
        val tracks = getTrackListUseCase(query)
        emit(createSuccessState(tracks))
    }.catch { emit(SearchUiState.Error(SearchFragmentErrors.InternetConnection())) }

    private fun createSuccessState(tracks: List<Track>): SearchUiState {
        return if (tracks.isEmpty()) {
            SearchUiState.Error(SearchFragmentErrors.EmptyResponse())
        } else {
            SearchUiState.WebTracks(tracks)
        }
    }

    companion object {
        fun getFactory(
            getTrackListUseCase: GetTrackListUseCase,
            getHistoryListUseCase: GetHistoryListUseCase,
            addTrackToSearchHistoryUseCase: AddTrackToSearchHistoryUseCase,
            clearHistoryUseCase: ClearHistoryUseCase
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SearchViewModel(
                        getTrackListUseCase,
                        getHistoryListUseCase,
                        addTrackToSearchHistoryUseCase,
                        clearHistoryUseCase
                    )
                }
            }
    }
}