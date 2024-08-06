package com.android.playlistmaker.settings.domain


interface ToggleThemeUseCaseInterface {
    fun execute(isDarkModeEnabled: Boolean)
}