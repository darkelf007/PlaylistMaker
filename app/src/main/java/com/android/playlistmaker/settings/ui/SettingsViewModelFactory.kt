package com.android.playlistmaker.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.playlistmaker.creator.Creator
import com.android.playlistmaker.settings.data.CommunicationRepository
import com.android.playlistmaker.settings.data.SettingsRepository
import com.android.playlistmaker.settings.util.IntentUtils

@Suppress("UNCHECKED_CAST")
class SettingsViewModelFactory(
    private val repository: SettingsRepository,
    private val communicationRepository: CommunicationRepository,
    private val intentUtils: IntentUtils
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val getThemeUseCase = Creator.createGetThemeUseCase(repository)
        val toggleThemeUseCase = Creator.createToggleThemeUseCase(repository)
        val showUserAgreementUseCase =
            Creator.createShowUserAgreementUseCase(communicationRepository, intentUtils)
        val sendSupportEmailUseCase =
            Creator.createSendSupportEmailUseCase(communicationRepository, intentUtils)
        val showShareDialogUseCase =
            Creator.createShowShareDialogUseCase(communicationRepository, intentUtils)
        return SettingsViewModel(getThemeUseCase, toggleThemeUseCase ,showUserAgreementUseCase,
            sendSupportEmailUseCase,
            showShareDialogUseCase) as T
    }
}