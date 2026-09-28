package com.example.playlistmaker.data.repositories

import com.example.playlistmaker.data.local.StorageClient
import com.example.playlistmaker.domain.repository.LocalThemeRepository

class LocalThemeRepositoryImpl(
    private val storage: StorageClient<Boolean>
) : LocalThemeRepository {

    override fun getThemeValue(): Boolean {
        return storage.getData() ?: false
    }

    override fun setThemeValue(isNight: Boolean) {
        storage.saveData(isNight)
    }
}