package com.android.playlistmaker.settings.domain

interface SettingsRepository {
    fun setDarkThemeEnabled(enabled: Boolean)
    fun getDarkThemeEnabled(): Boolean
}

