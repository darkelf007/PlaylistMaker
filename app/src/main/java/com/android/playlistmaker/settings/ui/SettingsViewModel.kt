package com.android.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.playlistmaker.settings.domain.GetThemeUseCase
import com.android.playlistmaker.settings.domain.ToggleThemeUseCase

class SettingsViewModel(
    private val getThemeUseCase: GetThemeUseCase,
    private val toggleThemeUseCase: ToggleThemeUseCase
) : ViewModel() {

    private val _darkThemeEnabled = MutableLiveData<Boolean>()
    val darkThemeEnabled: LiveData<Boolean> get() = _darkThemeEnabled

    init {
        _darkThemeEnabled.value = isDarkThemeEnabled()
    }

    fun switchTheme(isDark: Boolean) {
        toggleThemeUseCase.execute(isDark)
        _darkThemeEnabled.value = isDark
    }


    private fun isDarkThemeEnabled(): Boolean {
        return getThemeUseCase.execute()
    }
}