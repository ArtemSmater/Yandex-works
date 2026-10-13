package com.example.playlistmaker.presentation.viewmodelutils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val creators: @JvmSuppressWildcards
    Map<Class<out ViewModel>, ViewModelCreator>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        val creator = creators[modelClass]
            ?: throw IllegalArgumentException(
                "Unknown ViewModel: ${modelClass.name}"
            )

        @Suppress("UNCHECKED_CAST")
        return creator.create(extras) as T
    }
}