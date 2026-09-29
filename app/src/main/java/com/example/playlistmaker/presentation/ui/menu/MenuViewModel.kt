package com.example.playlistmaker.presentation.ui.menu

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class MenuViewModel : ViewModel() {

    private val _menuUiActions = PublishSubject.create<MenuUiActions>()
    val menuUiActions: Observable<MenuUiActions> = _menuUiActions.hide()

    fun uiActions(action: MenuUiActions) {
        when (action) {
            is MenuUiActions.LaunchSearchFragment -> {
                _menuUiActions.onNext(MenuUiActions.LaunchSearchFragment)
            }

            is MenuUiActions.LaunchMediaFragment -> {
                _menuUiActions.onNext(MenuUiActions.LaunchMediaFragment)
            }

            is MenuUiActions.LaunchSettingsFragment -> {
                _menuUiActions.onNext(MenuUiActions.LaunchSettingsFragment)
            }
        }
    }
}