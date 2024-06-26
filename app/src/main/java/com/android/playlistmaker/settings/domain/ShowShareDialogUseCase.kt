package com.android.playlistmaker.settings.domain

import android.content.Intent
import com.android.playlistmaker.settings.data.CommunicationRepository
import com.android.playlistmaker.settings.util.IntentUtils

class ShowShareDialogUseCase(
    private val repository: CommunicationRepository,
    private val intentUtils: IntentUtils
) {
    fun execute(): Intent {
        val message = repository.getShareMessage()
        return Intent.createChooser(intentUtils.createShareIntent(message), null)
    }
}