package com.example.playlistmaker.presentation.viewmodel.searchviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.usecases.UpdateCacheUseCase
import com.example.playlistmaker.domain.usecases.GetCacheListUseCase
import com.example.playlistmaker.domain.usecases.GetTrackListUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SearchViewModel(
    private val getTrackListUseCase: GetTrackListUseCase,
    private val getCacheListUseCase: GetCacheListUseCase,
    private val updateCacheUseCase: UpdateCacheUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _trackListViewModel = MutableLiveData<List<Track>>()
    val trackListViewModel: LiveData<List<Track>>
        get() = _trackListViewModel

    private val _cacheListViewModel = MutableLiveData<List<Track>>()
    val cacheListViewModel: LiveData<List<Track>>
        get() = _cacheListViewModel

    private val _emptyResponseViewModel = MutableLiveData<Boolean>()
    val emptyResponseViewModel: LiveData<Boolean>
        get() = _emptyResponseViewModel

    private val _webErrorViewModel = MutableLiveData<Boolean>()
    val webErrorViewModel: LiveData<Boolean>
        get() = _webErrorViewModel


    // web methods
    fun getTrackList(entity: String) {
        val disposable = getTrackListUseCase(entity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (it.results?.isEmpty() == true) {
                        _emptyResponseViewModel.value = true
                    } else {
                        _trackListViewModel.value = it.results
                        _emptyResponseViewModel.value = false
                        _webErrorViewModel.value = false
                    }
                },
                { _webErrorViewModel.value = true }
            )
        compositeDisposable.add(disposable)
    }

    fun getEmptyList() {
        _trackListViewModel.value = emptyList()
    }

    // cache methods
    fun getCacheList() {
        _cacheListViewModel.value = getCacheListUseCase()
    }

    fun saveCache(tracks: List<Track>) {
        updateCacheUseCase(tracks)
    }

    fun getEmptyCache() {
        _cacheListViewModel.value = emptyList()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}