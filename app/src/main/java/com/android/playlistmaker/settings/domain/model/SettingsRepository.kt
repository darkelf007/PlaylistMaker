package com.android.playlistmaker.settings.domain.model

import android.content.SharedPreferences
import com.android.playlistmaker.domain.app.App

class SettingsRepository(private val sharedPreferences: SharedPreferences) {

    fun getDarkThemeEnabled(): Boolean {
        return sharedPreferences.getBoolean(App.DARK_THEME_KEY, false)
    }

    fun setDarkThemeEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(App.DARK_THEME_KEY, enabled).apply()
    }
}


