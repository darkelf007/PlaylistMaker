package com.android.playlistmaker.settings.domain

class ShowShareDialogUseCase(private val repository: CommunicationRepositoryInterface) {
    fun execute(): String {
        return repository.getShareMessage()
    }
}