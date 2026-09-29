package com.example.playlistmaker.presentation.ui.player

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.presentation.ui.player.PlayerStates.DEFAULT
import com.example.playlistmaker.presentation.ui.player.PlayerStates.PAUSED
import com.example.playlistmaker.presentation.ui.player.PlayerStates.PLAYING
import com.example.playlistmaker.presentation.ui.player.PlayerStates.PREPARED
import com.example.playlistmaker.presentation.utils.Transform
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class PlayerViewModel(
    private val track: Track
) : ViewModel() {

    private val mediaPlayer by lazy {
        MediaPlayer()
    }
    private var playerState = DEFAULT

    // for ui state subscribers
    private val _playerViewModelState = MutableStateFlow<PlayerUiState>(PlayerUiState.Initial)
    val state = _playerViewModelState.asStateFlow()

    // for effects subscribers
    private val _playerViewModelEffect = MutableSharedFlow<PlayerUiEffect>(extraBufferCapacity = 1)
    val effect = _playerViewModelEffect.asSharedFlow()

    // progress subscriber object
    private var progressJob: Job? = null

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
        progressJob?.cancel()

        progressJob = viewModelScope.launch {
            while (isActive) {
                sendNewProgress(getPlayerProgress())
                delay(300.milliseconds)
            }
        }
    }

    private fun stopProgressChecking() {
        progressJob?.cancel()
        progressJob = null
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
        _playerViewModelState.value = PlayerUiState.Playing(getPlayerProgress())
        startProgressChecking()
    }

    private fun pause() {
        mediaPlayer.pause()
        playerState = PAUSED
        _playerViewModelState.value = PlayerUiState.Paused(getPlayerProgress())
        stopProgressChecking()
    }

    private fun backPressed() {
        _playerViewModelEffect.tryEmit(PlayerUiEffect.ClosePlayer)
    }

    private fun setListeners() {
        mediaPlayer.setOnPreparedListener {
            playerState = PREPARED
            _playerViewModelState.value = PlayerUiState.Prepared
        }

        mediaPlayer.setOnCompletionListener {
            playerState = PREPARED
            stopProgressChecking()
            _playerViewModelState.value = PlayerUiState.Prepared
        }
    }

    private fun sendNewProgress(progress: String) {
        _playerViewModelState.value = PlayerUiState.Playing(progress)
    }

    private fun getPlayerProgress(): String {
        val progress = mediaPlayer.currentPosition
        return Transform.millsToMins(progress.toLong())
    }


    override fun onCleared() {
        super.onCleared()
        stopProgressChecking()
        mediaPlayer.release()
    }

    companion object {
        fun getFactory(value: Track): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(value)
            }
        }
    }
}