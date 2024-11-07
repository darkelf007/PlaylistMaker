package com.android.playlistmaker.di

import com.android.playlistmaker.favorites_tracks.presentation.FavoriteFragmentViewModel
import com.android.playlistmaker.main.ui.MainViewModel
import com.android.playlistmaker.player.presentation.PlayerViewModel
import com.android.playlistmaker.search.presentation.ui.SearchViewModel
import com.android.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::SettingsViewModel)
    viewModel { PlayerViewModel(get(), get(), get(), get()) }
    viewModel { SearchViewModel(get(), get(), get()) }
    viewModel { FavoriteFragmentViewModel(get()) }
}