package com.example.playlistmaker.presentation.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.usecases.GetCacheListUseCase
import com.example.playlistmaker.domain.usecases.GetTrackListUseCase
import com.example.playlistmaker.domain.usecases.UpdateCacheUseCase
import com.example.playlistmaker.presentation.utils.Transform
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class SearchViewModel(
    private val getTrackListUseCase: GetTrackListUseCase,
    private val getCacheListUseCase: GetCacheListUseCase,
    private val updateCacheUseCase: UpdateCacheUseCase
) : ViewModel() {

    private lateinit var lastQuery: String
    private lateinit var cacheList: MutableList<Track>
    private val compositeDisposable = CompositeDisposable()

    // main observable query field
    private val queryValue = PublishSubject.create<String>()

    // double click security
    private val trackClick = PublishSubject.create<Track>()

    // for ui state subscribers
    private var _searchViewModelState = MutableLiveData<SearchUiState>(SearchUiState.Initial)
    val searchViewModelState: LiveData<SearchUiState>
        get() = _searchViewModelState

    // for effects subscribers
    private var _searchViewModelEffect = PublishSubject.create<SearchUiEffect>()
    val searchViewModelEffect: Observable<SearchUiEffect> = _searchViewModelEffect.hide()

    init {
        getTrackList()
        getCacheList()
        getTrackClick()
    }

    fun uiAction(action: SearchUiAction) {
        when (action) {
            is SearchUiAction.ClearCache -> {
                clearCache()
            }

            is SearchUiAction.RetryQuery -> {
                retryQuery()
            }

            is SearchUiAction.TrackClicked -> {
                trackClicked(action.track, action.isCached)
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
        _searchViewModelEffect.onNext(SearchUiEffect.BackPressed)
    }

    private fun fieldChanged(charSequence: CharSequence?, isFocused: Boolean) {
        if (charSequence == null) {
            _searchViewModelState.value = SearchUiState.Initial
            return
        }

        if (charSequence.isEmpty() && isFocused && cacheList.isNotEmpty()) {
            _searchViewModelState.value = SearchUiState.CacheTracks(cacheList)
            return
        }

        if (_searchViewModelState.value is SearchUiState.CacheTracks) {
            _searchViewModelState.value = SearchUiState.WebTracks(emptyList())
        }

        queryValue.onNext(charSequence.toString())
    }

    private fun trackClicked(track: Track, isCached: Boolean) {
        if (!isCached) changeCacheList(track)
        trackClick.onNext(track)
    }

    private fun getTrackClick() {
        trackClick
            .debounce(200, TimeUnit.MILLISECONDS)
            .subscribe { _searchViewModelEffect.onNext(SearchUiEffect.OpenPlayer(it)) }
            .let(compositeDisposable::add)
    }

    private fun retryQuery() {
        createSearchRequest(lastQuery)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _searchViewModelState.value = it }
            .let(compositeDisposable::add)
    }

    private fun clearCache() {
        cacheList.clear()
        _searchViewModelState.value = SearchUiState.CacheTracks(emptyList())
    }

    private fun changeCacheList(track: Track) {
        Transform.editList(track, cacheList)
    }

    private fun getCacheList() {
        cacheList = getCacheListUseCase().toMutableList()
    }

    private fun getTrackList() {
        queryValue
            .debounce(2, TimeUnit.SECONDS)
            .map { it.trim() }
            .distinctUntilChanged()
            .switchMap { createSearchObservable(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _searchViewModelState.value = it }
            .let(compositeDisposable::add)
    }

    private fun createSearchObservable(
        query: String
    ): Observable<SearchUiState> {
        return createSearchRequest(query).takeWhile { query.length > 2 }
    }

    private fun createSearchRequest(
        query: String
    ): Observable<SearchUiState> {
        lastQuery = query
        return getTrackListUseCase(query)
            .subscribeOn(Schedulers.io())
            .map { createSuccessState(it) }
            .toObservable()
            .startWith(SearchUiState.Loading)
            .onErrorReturn { SearchUiState.WebError }
    }

    private fun createSuccessState(tracks: List<Track>): SearchUiState {
        return if (tracks.isEmpty()) {
            SearchUiState.Empty
        } else {
            SearchUiState.WebTracks(tracks)
        }
    }

    override fun onCleared() {
        super.onCleared()
        updateCacheUseCase(cacheList)
        compositeDisposable.clear()
    }
}