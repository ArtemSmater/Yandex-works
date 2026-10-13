package com.example.playlistmaker.presentation.ui.player

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.playlistmaker.presentation.viewmodelutils.ViewModelCreator
import javax.inject.Inject

class PlayerViewModelCreator @Inject constructor(
    private val mediaPlayer: MediaPlayer
) : ViewModelCreator {
    override fun create(extras: CreationExtras): ViewModel {
        return PlayerViewModel(
            extras.createSavedStateHandle(),
            mediaPlayer
        )
    }
}