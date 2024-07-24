package com.android.playlistmaker.creator

import com.android.playlistmaker.settings.data.CommunicationRepository
import com.android.playlistmaker.settings.data.SettingsRepository
import com.android.playlistmaker.settings.domain.GetThemeUseCase
import com.android.playlistmaker.settings.domain.GetThemeUseCaseImpl
import com.android.playlistmaker.settings.domain.SendSupportEmailUseCase
import com.android.playlistmaker.settings.domain.ShowShareDialogUseCase
import com.android.playlistmaker.settings.domain.ShowUserAgreementUseCase
import com.android.playlistmaker.settings.domain.ToggleThemeUseCase
import com.android.playlistmaker.settings.domain.ToggleThemeUseCaseImpl
import com.android.playlistmaker.settings.util.IntentUtils

object Creator {
    fun createToggleThemeUseCase(repository: SettingsRepository): ToggleThemeUseCase {
        return ToggleThemeUseCaseImpl(repository)
    }


    fun createGetThemeUseCase(repository: SettingsRepository): GetThemeUseCase {
        return GetThemeUseCaseImpl(repository)
    }
    fun createShowUserAgreementUseCase(
        communicationRepository: CommunicationRepository,
        intentUtils: IntentUtils
    ): ShowUserAgreementUseCase {
        return ShowUserAgreementUseCase(communicationRepository, intentUtils)
    }

    fun createSendSupportEmailUseCase(
        communicationRepository: CommunicationRepository,
        intentUtils: IntentUtils
    ): SendSupportEmailUseCase {
        return SendSupportEmailUseCase(communicationRepository, intentUtils)
    }

    fun createShowShareDialogUseCase(
        communicationRepository: CommunicationRepository,
        intentUtils: IntentUtils
    ): ShowShareDialogUseCase {
        return ShowShareDialogUseCase(communicationRepository, intentUtils)
    }
}