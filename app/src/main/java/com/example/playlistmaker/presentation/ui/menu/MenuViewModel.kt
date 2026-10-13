package com.example.playlistmaker.presentation.ui.menu

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class MenuViewModel @Inject constructor() : ViewModel() {

    private val _actions = MutableSharedFlow<MenuUiActions>(extraBufferCapacity = 1)
    val actions = _actions.asSharedFlow()

    fun uiActions(action: MenuUiActions) {
        when (action) {
            is MenuUiActions.LaunchSearchFragment -> {
                _actions.tryEmit(MenuUiActions.LaunchSearchFragment)
            }

            is MenuUiActions.LaunchMediaFragment -> {
                _actions.tryEmit(MenuUiActions.LaunchMediaFragment)
            }

            is MenuUiActions.LaunchSettingsFragment -> {
                _actions.tryEmit(MenuUiActions.LaunchSettingsFragment)
            }
        }
    }
}