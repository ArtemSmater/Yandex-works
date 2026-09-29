package com.example.playlistmaker.presentation.ui.player

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.presentation.utils.Transform
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import com.example.playlistmaker.presentation.ui.player.PlayerStates.DEFAULT
import com.example.playlistmaker.presentation.ui.player.PlayerStates.PREPARED
import com.example.playlistmaker.presentation.ui.player.PlayerStates.PLAYING
import com.example.playlistmaker.presentation.ui.player.PlayerStates.PAUSED

class PlayerViewModel(
    private val track: Track
) : ViewModel() {

    private val mediaPlayer by lazy {
        MediaPlayer()
    }
    private var playerState = DEFAULT

    // for ui state subscribers
    private val _playerViewModelState = BehaviorSubject.create<PlayerUiState>()
    val playerViewModelState: Observable<PlayerUiState> = _playerViewModelState.hide()

    // for effects subscribers
    private val _playerViewModelEffect = PublishSubject.create<PlayerUiEffect>()
    val playerViewModelEffect: Observable<PlayerUiEffect> = _playerViewModelEffect.hide()

    // progress subscriber object
    private var progressDisposable: Disposable? = null

    init {
        preparePlayer()
    }


    fun uiAction(action: PlayerUiAction) {
        when (action) {
            is PlayerUiAction.Play -> {
                playbackControl()
            }

            is PlayerUiAction.Back -> {
                backPressed()
            }

            is PlayerUiAction.Pause -> {
                checkPlaying()
            }
        }
    }

    private fun checkPlaying() {
        if (playerState == PLAYING) {
            pause()
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        setListeners()
    }

    private fun startProgressChecking() {
        progressDisposable = Observable
            .interval(300, java.util.concurrent.TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { sendNewProgress(getPlayerProgress()) }
    }

    private fun stopProgressChecking() {
        progressDisposable?.dispose()
    }

    private fun playbackControl() {
        when (playerState) {
            PLAYING -> pause()
            PAUSED, PREPARED -> play()
            else -> throw RuntimeException("Unknown player state!")
        }
    }

    private fun play() {
        mediaPlayer.start()
        playerState = PLAYING
        _playerViewModelState.onNext(PlayerUiState.Playing(getPlayerProgress()))
        startProgressChecking()
    }

    private fun pause() {
        mediaPlayer.pause()
        playerState = PAUSED
        _playerViewModelState.onNext(PlayerUiState.Paused(getPlayerProgress()))
        stopProgressChecking()
    }

    private fun backPressed() {
        _playerViewModelEffect.onNext(PlayerUiEffect.ClosePlayer)
    }

    private fun setListeners() {
        mediaPlayer.setOnPreparedListener {
            playerState = PREPARED
            _playerViewModelState.onNext(PlayerUiState.Prepared)
        }

        mediaPlayer.setOnCompletionListener {
            playerState = PREPARED
            stopProgressChecking()
            _playerViewModelState.onNext(PlayerUiState.Prepared)
        }
    }

    private fun sendNewProgress(progress: String) {
        _playerViewModelState.onNext(PlayerUiState.Playing(progress))
    }

    private fun getPlayerProgress(): String {
        val progress = mediaPlayer.currentPosition
        return Transform.millsToMins(progress.toLong())
    }


    override fun onCleared() {
        super.onCleared()
        stopProgressChecking()
        mediaPlayer.release()
        progressDisposable = null
    }

    companion object {
        fun getFactory(value: Track): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(value)
            }
        }
    }
}