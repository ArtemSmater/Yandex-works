package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.repositories.WebRepositoryImpl
import com.example.playlistmaker.domain.repository.WebRepository

object WebRepositoryProvider {

    private var webRepositoryImpl: WebRepositoryImpl? = null

    fun provideWebRepository() : WebRepository {

        if (webRepositoryImpl == null) {
            val server = ApiFactory.apiService
            webRepositoryImpl = WebRepositoryImpl(server)
        }
        return webRepositoryImpl!!
    }
}
