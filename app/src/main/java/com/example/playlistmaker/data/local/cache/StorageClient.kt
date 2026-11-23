package com.example.playlistmaker.data.local.cache

interface StorageClient<T> {
    fun saveData(data: T)
    fun getData(): T?
}