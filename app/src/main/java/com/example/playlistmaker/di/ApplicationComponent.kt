package com.example.playlistmaker.di

import android.app.Application
import com.example.playlistmaker.di.annotations.ApplicationScope
import com.example.playlistmaker.domain.usecases.GetThemeUseCase
import com.example.playlistmaker.presentation.ui.media.fragments.favourite.FavouriteFragment
import com.example.playlistmaker.presentation.ui.media.fragments.playlist.PlaylistFragment
import com.example.playlistmaker.presentation.ui.menu.MenuFragment
import com.example.playlistmaker.presentation.ui.player.PlayerFragment
import com.example.playlistmaker.presentation.ui.search.SearchFragment
import com.example.playlistmaker.presentation.ui.settings.SettingsFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, DomainModule::class, PresentationModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(fragment: MenuFragment)
    fun inject(fragment: PlayerFragment)
    fun inject(fragment: SearchFragment)
    fun inject(fragment: SettingsFragment)
    fun inject(fragment: FavouriteFragment)
    fun inject(fragment: PlaylistFragment)
    fun getThemeUseCase(): GetThemeUseCase

    @Component.Factory
    interface ApplicationComponentFactory {
        fun create(@BindsInstance application: Application): ApplicationComponent
    }
}