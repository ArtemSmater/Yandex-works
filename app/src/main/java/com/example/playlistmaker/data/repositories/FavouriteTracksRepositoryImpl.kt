package com.example.playlistmaker.data.repositories

import com.example.playlistmaker.data.local.database.dao.FavouritesDao
import com.example.playlistmaker.data.mapper.mapDomainToEntity
import com.example.playlistmaker.data.mapper.mapEntityListToDomainList
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.repository.FavouriteTracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavouriteTracksRepositoryImpl @Inject constructor(
    private val favouritesDao: FavouritesDao
) : FavouriteTracksRepository {


    override fun getFavouriteTracks(): Flow<List<Track>> {
        return favouritesDao.getFavouritesList().map { it.mapEntityListToDomainList().reversed() }
    }

    override fun checkFavouriteTrack(trackId: String): Flow<Boolean> {
        return favouritesDao.isTrackExisted(trackId)
    }

    override suspend fun toggleFavourite(track: Track) {
        favouritesDao.toggleFavourite(track.mapDomainToEntity())
    }
}