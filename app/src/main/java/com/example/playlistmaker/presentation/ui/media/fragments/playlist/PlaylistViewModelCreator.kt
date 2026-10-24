package com.example.playlistmaker.presentation.ui.media.fragments.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.playlistmaker.presentation.viewmodelutils.ViewModelCreator
import javax.inject.Inject
import javax.inject.Provider

class PlaylistViewModelCreator @Inject constructor(
    private val provider: Provider<PlaylistViewModel>
) : ViewModelCreator {
    override fun create(extras: CreationExtras): ViewModel {
        return provider.get()
    }
}