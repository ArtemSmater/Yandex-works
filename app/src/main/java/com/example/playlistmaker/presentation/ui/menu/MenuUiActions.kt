package com.example.playlistmaker.presentation.ui.menu

sealed interface MenuUiActions {
    object LaunchSearchFragment : MenuUiActions
    object LaunchMediaFragment : MenuUiActions
    object LaunchSettingsFragment : MenuUiActions
}