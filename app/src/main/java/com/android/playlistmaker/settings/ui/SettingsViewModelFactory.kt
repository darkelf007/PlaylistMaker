package com.android.playlistmaker.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.playlistmaker.creator.Creator
import com.android.playlistmaker.settings.data.SettingsRepository

@Suppress("UNCHECKED_CAST")
class SettingsViewModelFactory(private val repository: SettingsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val getThemeUseCase = Creator.createGetThemeUseCase(repository)
            val toggleThemeUseCase = Creator.createToggleThemeUseCase(repository)
            return SettingsViewModel(getThemeUseCase, toggleThemeUseCase) as T
    }
}