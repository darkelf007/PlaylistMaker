package com.android.playlistmaker.creator

import android.content.Context
import android.media.MediaPlayer
import com.android.playlistmaker.player.presentation.PlayerViewModelFactory
import com.android.playlistmaker.settings.data.CommunicationRepositoryImpl
import com.android.playlistmaker.settings.domain.SendSupportEmailUseCase
import com.android.playlistmaker.settings.domain.ShowShareDialogUseCase
import com.android.playlistmaker.settings.domain.ShowUserAgreementUseCase

object Creator {

    fun createShowUserAgreementUseCase(context: Context): ShowUserAgreementUseCase {
        val repository = CommunicationRepositoryImpl(context)
        return ShowUserAgreementUseCase(repository)
    }

    fun createSendSupportEmailUseCase(context: Context): SendSupportEmailUseCase {
        val repository = CommunicationRepositoryImpl(context)
        return SendSupportEmailUseCase(repository)
    }

    fun createShowShareDialogUseCase(context: Context): ShowShareDialogUseCase {
        val repository = CommunicationRepositoryImpl(context)
        return ShowShareDialogUseCase(repository)
    }

    fun createPlayerViewModelFactory(): PlayerViewModelFactory {
        val mediaPlayer = MediaPlayer()
        return PlayerViewModelFactory(mediaPlayer)
    }
}