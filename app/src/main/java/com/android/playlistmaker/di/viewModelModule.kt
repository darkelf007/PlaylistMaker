package com.android.playlistmaker.di

import android.app.Application
import com.android.playlistmaker.main.ui.MainViewModel
import com.android.playlistmaker.media.presentation.FavoriteFragmentViewModel
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
    viewModel { (application: Application) -> SearchViewModel(application, get(), get()) }
    viewModel { FavoriteFragmentViewModel(get(), get()) }
}