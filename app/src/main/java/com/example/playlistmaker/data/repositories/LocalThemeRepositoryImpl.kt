package com.example.playlistmaker.data.repositories

import android.app.Application
import com.example.playlistmaker.App
import com.example.playlistmaker.data.local.StorageClient
import com.example.playlistmaker.domain.repository.LocalThemeRepository
import javax.inject.Inject

class LocalThemeRepositoryImpl @Inject constructor(
    private val application: Application,
    private val storage: StorageClient<Boolean>
) : LocalThemeRepository {

    override fun getThemeValue(): Boolean {
        return storage.getData() ?: false
    }

    override fun setThemeValue(isNight: Boolean) {
        (application as App).switchTheme(isNight)
        storage.saveData(isNight)
    }
}