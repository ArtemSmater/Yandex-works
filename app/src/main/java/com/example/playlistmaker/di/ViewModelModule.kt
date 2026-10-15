package com.example.playlistmaker.di

import com.example.playlistmaker.di.annotations.ViewModelKey
import com.example.playlistmaker.presentation.ui.menu.MenuViewModel
import com.example.playlistmaker.presentation.ui.menu.MenuViewModelCreator
import com.example.playlistmaker.presentation.ui.player.PlayerViewModel
import com.example.playlistmaker.presentation.ui.player.PlayerViewModelCreator
import com.example.playlistmaker.presentation.ui.search.SearchViewModel
import com.example.playlistmaker.presentation.ui.search.SearchViewModelCreator
import com.example.playlistmaker.presentation.ui.settings.SettingsViewModel
import com.example.playlistmaker.presentation.ui.settings.SettingsViewModelCreator
import com.example.playlistmaker.presentation.viewmodelutils.ViewModelCreator
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MenuViewModel::class)
    fun bindMenuViewModelCreator(creator: MenuViewModelCreator): ViewModelCreator

    @Binds
    @IntoMap
    @ViewModelKey(PlayerViewModel::class)
    fun bindPlayerViewModelCreator(creator: PlayerViewModelCreator): ViewModelCreator

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    fun bindSearchViewModelCreator(creator: SearchViewModelCreator): ViewModelCreator

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    fun bindSettingsViewModelCreator(creator: SettingsViewModelCreator): ViewModelCreator
}