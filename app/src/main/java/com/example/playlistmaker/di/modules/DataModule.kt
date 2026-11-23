package com.example.playlistmaker.di.modules

import android.app.Application
import androidx.room.Room
import com.example.playlistmaker.data.local.cache.PrefsStorageClient
import com.example.playlistmaker.data.local.cache.StorageClient
import com.example.playlistmaker.data.local.database.AppDatabase
import com.example.playlistmaker.data.local.database.dao.FavouritesDao
import com.example.playlistmaker.data.local.database.tables.TrackEntity
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

        private const val LOCAL_DATABASE_NAME = "playlist_maker_database.db"

        @Provides
        @ApplicationScope
        fun provideLocalDatabase(
            application: Application
        ): AppDatabase {
            return Room.databaseBuilder(
                application,
                AppDatabase::class.java,
                LOCAL_DATABASE_NAME
            ).build()
        }

        @Provides
        @ApplicationScope
        fun provideFavouritesDao(
            database: AppDatabase
        ): FavouritesDao {
            return database.getFavouritesDao()
        }

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