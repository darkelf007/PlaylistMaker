package com.android.playlistmaker.settings.data

import android.content.SharedPreferences
import com.android.playlistmaker.domain.app.App
import com.android.playlistmaker.domain.app.App.Companion.DARK_THEME_KEY

class SettingsRepository(private val sharedPreferences: SharedPreferences) {

    fun setDarkThemeEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(DARK_THEME_KEY, enabled).apply()
    }

    fun getDarkThemeEnabled(): Boolean {
        return sharedPreferences.getBoolean(DARK_THEME_KEY, false)
    }

}


