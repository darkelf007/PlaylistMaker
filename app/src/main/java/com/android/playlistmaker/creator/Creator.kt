package com.android.playlistmaker.creator

import com.android.playlistmaker.settings.data.SettingsRepository
import com.android.playlistmaker.settings.domain.GetThemeUseCase
import com.android.playlistmaker.settings.domain.GetThemeUseCaseImpl
import com.android.playlistmaker.settings.domain.ToggleThemeUseCase
import com.android.playlistmaker.settings.domain.ToggleThemeUseCaseImpl

object Creator {
    fun createToggleThemeUseCase(repository: SettingsRepository): ToggleThemeUseCase {
        return ToggleThemeUseCaseImpl(repository)
    }


    fun createGetThemeUseCase(repository: SettingsRepository): GetThemeUseCase {
        return GetThemeUseCaseImpl(repository)
    }

}