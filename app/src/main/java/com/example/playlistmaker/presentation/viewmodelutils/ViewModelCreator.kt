package com.example.playlistmaker.presentation.viewmodelutils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras

interface ViewModelCreator {
    fun create(extras: CreationExtras): ViewModel
}