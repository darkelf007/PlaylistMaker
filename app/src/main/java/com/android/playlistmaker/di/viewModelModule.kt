package com.android.playlistmaker.di

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.android.playlistmaker.main.ui.MainViewModel
import com.android.playlistmaker.player.presentation.PlayerViewModel
import com.android.playlistmaker.search.presentation.ui.SearchViewModel
import com.android.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { PlayerViewModel(get(), SavedStateHandle()) }
    viewModel { (application: Application) -> SearchViewModel(application, get(), get()) }
    viewModel {
        SettingsViewModel(
            getThemeUseCase = get(),
            toggleThemeUseCase = get(),
            showUserAgreementUseCase = get(),
            sendSupportEmailUseCase = get(),
            showShareDialogUseCase = get()
        )
    }
}