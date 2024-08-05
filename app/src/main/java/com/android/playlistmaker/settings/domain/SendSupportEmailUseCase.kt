package com.android.playlistmaker.settings.domain


class SendSupportEmailUseCase(private val repository: CommunicationRepositoryInterface) {
    fun execute(): EmailDetails {
        return repository.getSupportEmailDetails()
    }
}