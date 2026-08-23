package com.example.playlistmaker.data.localcache

import android.content.Context
import com.example.playlistmaker.data.repositories.CacheRepositoryImpl
import com.example.playlistmaker.domain.repository.CacheRepository

object CacheRepositoryProvider {

    private var cacheRepository: CacheRepository? = null

    fun provideCacheRepository(
        context: Context
    ) : CacheRepository {

        if (cacheRepository == null) {
            val storage = PreferenceStorage(context.applicationContext)
            cacheRepository = CacheRepositoryImpl(storage)
        }
        return cacheRepository!!
    }
}