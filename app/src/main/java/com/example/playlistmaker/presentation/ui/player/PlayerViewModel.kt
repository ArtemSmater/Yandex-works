package com.example.playlistmaker.presentation.ui.player

import android.media.MediaPlayer
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.usecases.CheckFavouriteTrackUseCase
import com.example.playlistmaker.domain.usecases.ToggleFavouriteUseCase
import com.example.playlistmaker.presentation.ui.player.PlayerStates.DEFAULT
import com.example.playlistmaker.presentation.ui.player.PlayerStates.PAUSED
import com.example.playlistmaker.presentation.ui.player.PlayerStates.PLAYING
import com.example.playlistmaker.presentation.ui.player.PlayerStates.PREPARED
import com.example.playlistmaker.presentation.utils.millsToMins
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.android.asCoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class PlayerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mediaPlayerFactory: () -> MediaPlayer,
    checkFavouriteTrackUseCase: CheckFavouriteTrackUseCase,
    private val toggleFavouriteUseCase: ToggleFavouriteUseCase
) : ViewModel() {

    private lateinit var mediaPlayer: MediaPlayer

    private val track: Track = checkNotNull(savedStateHandle["track"]) {
        "Track was not passed to PlayerViewModel"
    }
    private val playerThread = HandlerThread("Player thread").apply {
        start()
    }
    private val playerHandler = Handler(playerThread.looper)
    private val playerDispatcher =
        playerHandler.asCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(
        playerDispatcher + SupervisorJob()
    )

    private var playerState = DEFAULT

    private var progressJob: Job? = null
    private var playerControlJob: Job? = null

    // for ui state subscribers
    val favouriteObservable = checkFavouriteTrackUseCase(track.trackId)
    private val _playerViewModelState = MutableStateFlow<PlayerUiState>(PlayerUiState.Initial)
    val state = _playerViewModelState.asStateFlow()

    init {
        coroutineScope.launch {
            mediaPlayer = mediaPlayerFactory()
            preparePlayer()
        }
    }

    fun uiAction(action: PlayerUiAction) {
        when (action) {
            is PlayerUiAction.Play,
            is PlayerUiAction.Pause -> {
                playerControlJob?.cancel()
                playerControlJob = coroutineScope.launch {
                    when (action) {
                        is PlayerUiAction.Play -> playbackControl()
                        is PlayerUiAction.Pause -> checkPlaying()
                    }
                }
            }

            is PlayerUiAction.ToggleFavourite -> {
                viewModelScope.launch {
                    toggleFavouriteUseCase(track)
                }
            }
        }
    }

    private fun checkPlaying() {
        if (playerState == PLAYING) {
            pause()
        }
    }

    private fun preparePlayer() {
        setListeners()
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
    }

    private fun startProgressChecking() {
        var counter = 0
        progressJob?.cancel()
        progressJob = coroutineScope.launch {
            while (isActive && playerState == PLAYING) {
                Log.i("MY_TAG", "${counter++}")
                sendNewProgress(getPlayerProgress())
                delay(PLAYER_DELAY_VALUE.milliseconds)
            }
        }
    }

    private fun stopProgressChecking() {
        progressJob?.cancel()
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

        mediaPlayer.setOnErrorListener { _, _, _ ->
            stopProgressChecking()
            playerState = DEFAULT
            _playerViewModelState.value = PlayerUiState.Error("Unable to play track!")
            true
        }
    }

    private fun sendNewProgress(progress: String) {
        _playerViewModelState.value = PlayerUiState.Playing(progress)
    }

    private fun getPlayerProgress(): String {
        val progress = mediaPlayer.currentPosition
        return progress.toLong().millsToMins()
    }

    private fun releasePlayer() {
        stopProgressChecking()

        mediaPlayer.apply {
            setOnPreparedListener(null)
            setOnCompletionListener(null)
            setOnErrorListener(null)
            release()
        }
    }


    override fun onCleared() {
        coroutineScope.cancel()
        playerHandler.post {
            try {
                if (::mediaPlayer.isInitialized) {
                    releasePlayer()
                    playerState = DEFAULT
                }
            } finally {
                playerThread.quitSafely()
            }
        }
    }

    companion object {
        private const val PLAYER_DELAY_VALUE = 300L
    }
}