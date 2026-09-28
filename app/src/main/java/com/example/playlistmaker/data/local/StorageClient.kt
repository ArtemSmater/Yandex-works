package com.example.playlistmaker.data.local

interface StorageClient<T> {
    fun saveData(data: T)
    fun getData(): T?
}