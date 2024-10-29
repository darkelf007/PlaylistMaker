package com.android.playlistmaker.settings.domain


class ToggleThemeUseCase(private val settingsRepository: SettingsRepository) :
    ToggleThemeUseCaseInterface {
    override fun execute(isDarkModeEnabled: Boolean) {
        settingsRepository.setDarkThemeEnabled(isDarkModeEnabled)
    }
}