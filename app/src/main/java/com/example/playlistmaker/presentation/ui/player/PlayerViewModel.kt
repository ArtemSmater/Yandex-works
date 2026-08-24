package com.example.playlistmaker.presentation.ui.player

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.presentation.utils.Transform
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

class PlayerViewModel(val track: Track) : ViewModel() {

    private val mediaPlayer: MediaPlayer by lazy {
        MediaPlayer()
    }

    private var playerState = STATE_DEFAULT

    // for ui state subscribers
    private val _playerViewModelState = MutableLiveData<PlayerUiState>(PlayerUiState.Initial)
    val playerViewModelState: LiveData<PlayerUiState>
        get() = _playerViewModelState

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

            is PlayerUiAction.Release -> {
                turnOffPlayer()
            }

            is PlayerUiAction.Back -> {
                backPressed()
            }

            is PlayerUiAction.Pause -> {
                checkPlaying()
            }
        }
    }

    private fun turnOffPlayer() {
        mediaPlayer.release()
    }

    private fun checkPlaying() {
        if (playerState == STATE_PLAYING) {
            pause()
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        setListeners()
    }

    private fun startProgressChecking() {
        progressDisposable?.dispose()
        progressDisposable = Observable
            .interval(300, java.util.concurrent.TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { sendNewProgress(getPlayerProgress()) }
    }

    private fun stopProgressChecking() {
        progressDisposable?.dispose()
        progressDisposable = null
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> pause()
            STATE_PAUSED, STATE_PREPARED -> play()
        }
    }

    private fun play() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        _playerViewModelState.value = PlayerUiState.Playing(getPlayerProgress())
        startProgressChecking()
    }

    private fun pause() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        _playerViewModelState.value = PlayerUiState.Paused(getPlayerProgress())
        stopProgressChecking()
    }

    private fun backPressed() {
        _playerViewModelEffect.onNext(PlayerUiEffect.ClosePlayer)
    }

    private fun setListeners() {
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
            _playerViewModelState.value = PlayerUiState.Prepared(
                Transform.millsToMins(mediaPlayer.duration.toLong())
            )
        }

        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            stopProgressChecking()
            _playerViewModelState.value = PlayerUiState.Prepared(
                Transform.millsToMins(mediaPlayer.duration.toLong())
            )
        }
    }

    private fun sendNewProgress(progress: String) {
        _playerViewModelState.value = PlayerUiState.Playing(progress)
    }

    private fun getPlayerProgress(): String {
        val progress = mediaPlayer.duration - mediaPlayer.currentPosition
        return Transform.millsToMins(progress.toLong())
    }


    override fun onCleared() {
        super.onCleared()
        stopProgressChecking()
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}