package com.android.playlistmaker.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.playlistmaker.settings.data.SettingsRepository
import com.android.playlistmaker.settings.domain.GetThemeUseCaseImpl
import com.android.playlistmaker.settings.domain.SendSupportEmailUseCase
import com.android.playlistmaker.settings.domain.ShowShareDialogUseCase
import com.android.playlistmaker.settings.domain.ShowUserAgreementUseCase
import com.android.playlistmaker.settings.domain.ToggleThemeUseCaseImpl


class SettingsViewModelFactory(
    private val settingsRepository: SettingsRepository,
    private val sendSupportEmailUseCase: SendSupportEmailUseCase,
    private val showUserAgreementUseCase: ShowUserAgreementUseCase,
    private val showShareDialogUseCase: ShowShareDialogUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val getThemeUseCase = GetThemeUseCaseImpl(settingsRepository)
        val toggleThemeUseCase = ToggleThemeUseCaseImpl(settingsRepository)
        return SettingsViewModel(
            getThemeUseCase,
            toggleThemeUseCase,
            showUserAgreementUseCase,
            sendSupportEmailUseCase,
            showShareDialogUseCase
        ) as T
    }
}