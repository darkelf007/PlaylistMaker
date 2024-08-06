package com.android.playlistmaker.settings.domain

class GetThemeUseCase(private val settingsRepository: SettingsRepository) : GetThemeUseCaseInterface {
    override fun execute(): Boolean {
        return settingsRepository.getDarkThemeEnabled()
    }
}