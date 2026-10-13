package com.example.playlistmaker.di

import android.app.Application
import com.example.playlistmaker.data.dto.local.TrackEntity
import com.example.playlistmaker.data.local.PrefsStorageClient
import com.example.playlistmaker.data.local.StorageClient
import com.example.playlistmaker.data.remote.NetworkClient
import com.example.playlistmaker.data.remote.NetworkFactory
import com.example.playlistmaker.di.annotations.ApplicationScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    companion object {
        @Provides
        @ApplicationScope
        fun provideGson(): Gson = Gson()

        @Provides
        @ApplicationScope
        fun provideSearchHistoryStorageClient(
            application: Application,
            gson: Gson
        ): StorageClient<List<TrackEntity>> {
            val type = object : TypeToken<ArrayList<TrackEntity>>() {}.type
            return PrefsStorageClient(
                application,
                PrefsStorageClient.SHARED_KEY_CACHE,
                type,
                gson
            )
        }

        @Provides
        @ApplicationScope
        fun provideLocalThemeStorageClient(
            application: Application,
            gson: Gson
        ): StorageClient<Boolean> {
            val type = object : TypeToken<Boolean>() {}.type
            return PrefsStorageClient(
                application,
                PrefsStorageClient.SHARED_KEY_THEME,
                type,
                gson
            )
        }

        @Provides
        @ApplicationScope
        fun provideNetworkClient(): NetworkClient {
            return NetworkFactory.apiService
        }
    }
}