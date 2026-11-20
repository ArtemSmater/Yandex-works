package com.example.playlistmaker.di.modules

import android.media.MediaPlayer
import dagger.Module
import dagger.Provides

@Module
interface PresentationModule {

    companion object {
        @Provides
        fun provideMediaPlayer(): MediaPlayer = MediaPlayer()
    }
}