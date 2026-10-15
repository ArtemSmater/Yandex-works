package com.example.playlistmaker.di

import android.media.MediaPlayer
import dagger.Module
import dagger.Provides

@Module
class PresentationModule {

    @Provides
    fun provideMediaPlayer(): MediaPlayer = MediaPlayer()
}