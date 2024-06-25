package com.android.playlistmaker.domain.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.android.playlistmaker.settings.domain.model.SettingsRepository

class App : Application() {


    companion object {
        const val SETTING_PREFERENCES = "Setting_preferences"
        const val DARK_THEME_KEY = "Dark_theme_key"
    }
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences(SETTING_PREFERENCES, MODE_PRIVATE)
        settingsRepository = SettingsRepository(sharedPrefs)
    }

}