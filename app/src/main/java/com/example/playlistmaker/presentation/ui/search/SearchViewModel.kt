package com.example.playlistmaker.presentation.ui.search

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.usecases.AddTrackToSearchHistoryUseCase
import com.example.playlistmaker.domain.usecases.ClearHistoryUseCase
import com.example.playlistmaker.domain.usecases.GetHistoryListUseCase
import com.example.playlistmaker.domain.usecases.GetTrackListUseCase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class SearchViewModel(
    private val application: Application,
    private val getTrackListUseCase: GetTrackListUseCase,
    private val getHistoryListUseCase: GetHistoryListUseCase,
    private val addTrackToSearchHistoryUseCase: AddTrackToSearchHistoryUseCase,
    private val clearHistoryUseCase: ClearHistoryUseCase
) : ViewModel() {

    private lateinit var lastQuery: String
    private val compositeDisposable = CompositeDisposable()

    // main observable query field
    private val queryValue = PublishSubject.create<String>()

    // double click security
    private val trackClick = PublishSubject.create<Track>()

    // for ui state subscribers
    private var _searchViewModelState =
        BehaviorSubject.createDefault<SearchUiState>(SearchUiState.Initial)
    val searchViewModelState: Observable<SearchUiState> = _searchViewModelState.hide()

    // for effects subscribers
    private var _searchViewModelEffect = PublishSubject.create<SearchUiEffect>()
    val searchViewModelEffect: Observable<SearchUiEffect> = _searchViewModelEffect.hide()

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
        _searchViewModelEffect.onNext(SearchUiEffect.BackPressed)
    }

    private fun fieldChanged(charSequence: CharSequence?, isFocused: Boolean) {
        if (charSequence == null) {
            _searchViewModelState.onNext(SearchUiState.Initial)
            return
        }

        if (charSequence.isEmpty() && isFocused && getHistoryListUseCase().isNotEmpty()) {
            _searchViewModelState.onNext(SearchUiState.HistoryTracks(getHistoryListUseCase()))
            return
        }

        if (_searchViewModelState.value is SearchUiState.HistoryTracks) {
            _searchViewModelState.onNext(SearchUiState.WebTracks(emptyList()))
        }
        queryValue.onNext(charSequence.toString())
    }

    private fun trackClicked(track: Track) {
        addTrackToSearchHistoryUseCase(track)
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
            .subscribe { _searchViewModelState.onNext(it) }
            .let(compositeDisposable::add)
    }

    private fun clearTrackHistory() {
        clearHistoryUseCase()
        _searchViewModelState.onNext(SearchUiState.HistoryTracks(emptyList()))
    }

    private fun getTrackList() {
        queryValue
            .debounce(2, TimeUnit.SECONDS)
            .map { it.trim() }
            .distinctUntilChanged()
            .switchMap { createSearchObservable(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _searchViewModelState.onNext(it) }
            .let(compositeDisposable::add)
    }

    private fun createSearchObservable(query: String): Observable<SearchUiState> {
        return createSearchRequest(query).takeWhile { query.length > 2 }
    }

    private fun createSearchRequest(query: String): Observable<SearchUiState> {
        lastQuery = query
        return getTrackListUseCase(query)
            .subscribeOn(Schedulers.io())
            .map { createSuccessState(it) }
            .toObservable()
            .startWith(SearchUiState.Loading)
            .onErrorReturn {
                SearchUiState.Error(
                    SearchFragmentErrors.InternetConnection(
                        getErrorMessage(false).toString(),
                        getErrorDrawable(false)
                    )
                )
            }
    }

    private fun createSuccessState(tracks: List<Track>): SearchUiState {
        return if (tracks.isEmpty()) {
            SearchUiState.Error(
                SearchFragmentErrors.EmptyResponse(
                    getErrorMessage(true).toString(),
                    getErrorDrawable(true)
                )
            )
        } else {
            SearchUiState.WebTracks(tracks)
        }
    }

    private fun getErrorDrawable(isEmpty: Boolean): Drawable {
        return if (isEmpty) {
            application.theme.getDrawable(R.drawable.empty_light)
        } else {
            application.theme.getDrawable(R.drawable.error_light)
        }
    }

    private fun getErrorMessage(isEmpty: Boolean): StringBuilder {
        return if (isEmpty) {
            StringBuilder(application.getString(R.string.empty_list))
        } else {
            StringBuilder(application.getString(R.string.internet_error))
                .append(application.getString(R.string.spaces))
                .append(application.getString(R.string.download_error))
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
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
                    val app = (this[APPLICATION_KEY] as Application)
                    SearchViewModel(
                        app,
                        getTrackListUseCase,
                        getHistoryListUseCase,
                        addTrackToSearchHistoryUseCase,
                        clearHistoryUseCase
                    )
                }
            }
    }
}