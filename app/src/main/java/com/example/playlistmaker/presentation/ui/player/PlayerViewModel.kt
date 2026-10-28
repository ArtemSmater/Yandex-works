package com.example.playlistmaker.presentation.ui.player

import android.media.MediaPlayer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.presentation.ui.player.PlayerStates.DEFAULT
import com.example.playlistmaker.presentation.ui.player.PlayerStates.PAUSED
import com.example.playlistmaker.presentation.ui.player.PlayerStates.PLAYING
import com.example.playlistmaker.presentation.ui.player.PlayerStates.PREPARED
import com.example.playlistmaker.presentation.utils.millsToMins
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class PlayerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mediaPlayer: MediaPlayer
) : ViewModel() {

    private val track: Track = checkNotNull(savedStateHandle["track"]) {
        "Track was not passed to PlayerViewModel"
    }
    private var playerState = DEFAULT

    // for ui state subscribers
    private val _playerViewModelState = MutableStateFlow<PlayerUiState>(PlayerUiState.Initial)
    val state = _playerViewModelState.asStateFlow()

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
        return progress.toLong().millsToMins()
    }


    override fun onCleared() {
        stopProgressChecking()
        mediaPlayer.release()
    }
}