package com.android.playlistmaker.settings.domain

import com.android.playlistmaker.settings.data.SettingsRepository

class ToggleThemeUseCaseImpl(private val settingsRepository: SettingsRepository) : ToggleThemeUseCase {
    override fun execute(isDarkModeEnabled: Boolean) {
        settingsRepository.setDarkThemeEnabled(isDarkModeEnabled)
    }
}

class GetThemeUseCaseImpl(private val settingsRepository: SettingsRepository) : GetThemeUseCase {
    override fun execute(): Boolean {
        return settingsRepository.getDarkThemeEnabled()
    }
}