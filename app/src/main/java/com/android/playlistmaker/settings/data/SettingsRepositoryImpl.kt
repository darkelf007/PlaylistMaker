package com.android.playlistmaker.settings.data

import android.content.SharedPreferences
import com.android.playlistmaker.app.App.Companion.DARK_THEME_KEY
import com.android.playlistmaker.settings.domain.SettingsRepository

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences) : SettingsRepository {
    override fun setDarkThemeEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(DARK_THEME_KEY, enabled).apply()
    }

    override fun getDarkThemeEnabled(): Boolean {
        return sharedPreferences.getBoolean(DARK_THEME_KEY, false)
    }
}