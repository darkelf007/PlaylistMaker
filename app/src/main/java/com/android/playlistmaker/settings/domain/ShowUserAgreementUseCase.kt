package com.android.playlistmaker.settings.domain

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.android.playlistmaker.settings.data.CommunicationRepository
import com.android.playlistmaker.settings.util.IntentUtils

class ShowUserAgreementUseCase(
    private val repository: CommunicationRepository,
    private val intentUtils: IntentUtils
) {
    fun execute(): Intent {
        val url = repository.getUserAgreementUrl()
        return intentUtils.createWebIntent(url)
    }
}



