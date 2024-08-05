package com.android.playlistmaker.settings.domain

interface ToggleThemeUseCase {
    fun execute(isDarkModeEnabled: Boolean)
}

