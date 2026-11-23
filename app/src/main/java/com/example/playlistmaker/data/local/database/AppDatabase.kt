package com.example.playlistmaker.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.local.database.dao.FavouritesDao
import com.example.playlistmaker.data.local.database.tables.TrackEntity

@Database(
    entities = [TrackEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getFavouritesDao(): FavouritesDao
}