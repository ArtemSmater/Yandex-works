package com.example.playlistmaker.presentation.viewmodelutils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
    private val creators: @JvmSuppressWildcards
    Map<Class<out ViewModel>, Provider<ViewModelCreator>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        val creator = creators[modelClass]?.get()
            ?: throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")

        @Suppress("UNCHECKED_CAST")
        return creator.create(extras) as T
    }
}