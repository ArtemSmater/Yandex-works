package com.example.playlistmaker.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.playlistmaker.data.local.database.tables.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class FavouritesDao {

    @Query("SELECT * FROM saved_tracks")
    abstract fun getFavouritesList(): Flow<List<TrackEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM saved_tracks WHERE trackId =:trackId)")
    abstract fun isTrackExisted(trackId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertFavouriteTrack(trackEntity: TrackEntity)

    @Query("DELETE FROM saved_tracks WHERE trackId =:trackId")
    abstract suspend fun removeFavouriteTrack(trackId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM saved_tracks WHERE trackId = :trackId)")
    protected abstract suspend fun isTrackExistedOnce(trackId: String): Boolean

    @Transaction
    open suspend fun toggleFavourite(track: TrackEntity) {
        if (isTrackExistedOnce(track.trackId)) {
            removeFavouriteTrack(track.trackId)
        } else {
            insertFavouriteTrack(track)
        }
    }
}