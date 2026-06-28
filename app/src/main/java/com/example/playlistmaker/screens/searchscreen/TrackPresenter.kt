package com.example.playlistmaker.screens.searchscreen

import com.example.playlistmaker.interfaces.ErrorSubscriber
import com.example.playlistmaker.api.ApiFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TrackPresenter(val trackSubscriber: TrackSubscriber, val errorSubscriber: ErrorSubscriber) {

    private val service = ApiFactory.apiService
    private val compositeDisposable = CompositeDisposable()

    fun loadTracks(term: String) {
        val disposable = service.getSongs(term)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { trackSubscriber.trackList(it.results ?: emptyList()) },
                { errorSubscriber.showError(it.message.toString()) })
        compositeDisposable.add(disposable)
    }
}