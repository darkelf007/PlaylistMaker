package com.android.playlistmaker.settings.domain

import android.content.Intent
import com.android.playlistmaker.settings.data.CommunicationRepository
import com.android.playlistmaker.settings.util.IntentUtils

class SendSupportEmailUseCase(
    private val repository: CommunicationRepository,
    private val intentUtils: IntentUtils
) {
    fun execute(): Intent {
        val details = repository.getSupportEmailDetails()
        return intentUtils.createEmailIntent(details.email, details.subject, details.message)
    }
}