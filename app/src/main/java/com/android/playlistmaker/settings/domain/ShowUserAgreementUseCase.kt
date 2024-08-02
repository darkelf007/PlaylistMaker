package com.android.playlistmaker.settings.domain

class ShowUserAgreementUseCase(private val repository: CommunicationRepositoryInterface) {
    fun execute(): String {
        return repository.getUserAgreementUrl()
    }
}