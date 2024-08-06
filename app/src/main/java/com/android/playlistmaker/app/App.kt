package com.android.playlistmaker.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.android.playlistmaker.di.dataModule
import com.android.playlistmaker.di.interactorModule
import com.android.playlistmaker.di.playerModule
import com.android.playlistmaker.di.repositoryModule
import com.android.playlistmaker.di.viewModelModule
import com.android.playlistmaker.settings.domain.SettingsRepository
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    companion object {
        const val DARK_THEME_KEY = "Dark_theme_key"
    }

    private val settingsRepository: SettingsRepository by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(org.koin.core.logger.Level.DEBUG)
            androidContext(this@App)
            modules(
                listOf(
                    dataModule, interactorModule, repositoryModule, viewModelModule, playerModule
                )
            )
        }

        switchTheme(settingsRepository.getDarkThemeEnabled())

    }

    private fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}